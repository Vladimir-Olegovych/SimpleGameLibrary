package tools.eventbus

import tools.eventbus.annotation.BusEvent
import tools.eventbus.annotation.EventType
import java.lang.reflect.Method
import kotlin.reflect.KClass
import java.util.concurrent.CopyOnWriteArrayList

class EventBus {

    private val needProcessedEvents = mutableListOf<Pair<Any, KClass<*>?>>()
    private val handlers = CopyOnWriteArrayList<Any>()
    private val handlerCache = mutableMapOf<Class<*>, List<MethodInfo>>()

    data class MethodInfo(
        val method: Method,
        val eventType: KClass<*>?,
        val parameterType: Class<*>
    )

    fun process() {
        val iterator = needProcessedEvents.iterator()
        while (iterator.hasNext()) {
            val pair = iterator.next()
            applyEvent(pair.first, pair.second)
            iterator.remove()
        }
    }

    fun registerHandler(obj: Any) {
        handlers.add(obj)
        cacheHandlerMethods(obj)
    }

    fun removeHandler(obj: Any) {
        handlers.remove(obj)
        handlerCache.remove(obj::class.java)
    }

    fun clear() {
        handlers.clear()
        handlerCache.clear()
        needProcessedEvents.clear()
    }

    fun sendEventNow(event: Any, customType: KClass<*>? = null) {
        applyEvent(event, customType)
    }

    fun sendEvent(event: Any, customType: KClass<*>? = null) {
        needProcessedEvents.add(event to customType)
    }

    private fun applyEvent(event: Any, customType: KClass<*>?) {
        for (handler in handlers) {
            val methods = handlerCache[handler::class.java] ?: continue
            for (methodInfo in methods) {
                when {
                    methodInfo.eventType != null -> {
                        if (methodInfo.eventType == customType) {
                            methodInfo.method.invoke(handler, event)
                        }
                    }
                    methodInfo.parameterType == event::class.java -> {
                        methodInfo.method.invoke(handler, event)
                    }
                }
            }
        }
    }

    private fun cacheHandlerMethods(handler: Any) {
        val methods = handler::class.java.methods
        val methodInfos = methods.mapNotNull { method ->
            if (!method.isAnnotationPresent(BusEvent::class.java)) return@mapNotNull null

            val typeAnnotation = method.getAnnotation(EventType::class.java)
            val params = method.parameterTypes

            if (params.size != 1) return@mapNotNull null

            MethodInfo(
                method = method,
                eventType = typeAnnotation?.customType,
                parameterType = params[0]
            )
        }
        handlerCache[handler::class.java] = methodInfos
    }
}