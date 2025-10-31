package tools.eventbus

import tools.eventbus.annotation.BusEvent

class EventBus {

    private val handlers = ArrayList<Any>()

    fun registerHandler(obj: Any) = handlers.add(obj)

    fun removeHandler(obj: Any) = handlers.remove(obj)

    fun clear() = handlers.clear()

    fun sendEvent(event: Any): Boolean {
        return findFunctionToSend(event)
    }

    private fun findFunctionToSend(event: Any): Boolean {
        for (handler in handlers) {
            val methods = handler::class.java.methods
            for (method in methods) {
                if (method.isAnnotationPresent(BusEvent::class.java)) {
                    val params = method.parameterTypes
                    if (params.size == 1 && params[0] == event::class.java) {
                        method.invoke(handler, event)
                        return true
                    }
                }
            }
        }
        return false
    }
}