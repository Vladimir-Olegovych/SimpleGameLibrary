package tools.di

import com.artemis.annotations.Wire
import tools.di.module.ArtemisModule
import tools.di.module.annotations.DataName
import tools.di.module.annotations.ProvideData
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class ArtemisDI {
    private val instances = mutableMapOf<String, Any>()
    private val providers = mutableMapOf<String, () -> Any>()
    private val resolved = mutableSetOf<String>()

    fun start(modules: List<ArtemisModule>){
        modules.forEach { artemisModule ->
            registerModule(artemisModule)
        }
    }

    fun inject(any: Any) {
        val t = any.javaClass
        for (declaredField in t.declaredFields) {
            val annotation = runCatching {
                declaredField.getDeclaredAnnotation(Wire::class.java)
            }.getOrNull()
            if (annotation == null) continue
            try {
                val instance = getInstance(annotation.name)
                declaredField.isAccessible = true
                declaredField.set(any, instance)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }


    fun addInstance(instance: Any, customDataName: String? = null){
        val dataName = if (customDataName == null) {
            val instanceClass = instance::class
            val instanceDataName = instanceClass.findAnnotation<DataName>()?.name
            instanceDataName ?: instanceClass.simpleName.toString()
        } else {
            customDataName
        }
        instances[dataName] = instance
        resolved.add(dataName)
        addProvider(dataName, value = { instance })
    }

    private fun addProvider(name: String, value: () -> Any) {
        value.also { providers[name] = it }
    }

    private fun registerModule(module: ArtemisModule) {
        val moduleClass = module::class

        moduleClass.functions.forEach { function ->
            val provideAnnotation = function.findAnnotation<ProvideData>()
            if (provideAnnotation != null) {
                val dataNameAnnotation = function.findAnnotation<DataName>()
                val name = dataNameAnnotation?.name ?: function.name

                providers[name] = {
                    function.isAccessible = true
                    val parameters = resolveParameters(function, module)
                    function.call(module, *parameters) as Any
                }
            }
        }
    }

    private fun resolveParameters(function: KFunction<*>, module: Any): Array<Any?> {
        return function.parameters
            .filter { it.kind == KParameter.Kind.VALUE }
            .map { param ->
                val dataNameAnnotation = param.findAnnotation<DataName>()
                val name = dataNameAnnotation?.name

                if (name != null) {
                    getInstance(name)
                } else {
                    val typeName = param.type.toString()
                    instances.values.find { it::class.simpleName == typeName }
                }
            }
            .toTypedArray()
    }

    fun getInstance(name: String): Any {
        if (!instances.containsKey(name) && !resolved.contains(name)) {
            val provider = providers[name]
            if (provider != null) {
                resolved.add(name)
                instances[name] = provider()
            } else {
                throw IllegalArgumentException("No provider found for: $name")
            }
        }
        return instances[name] ?: throw IllegalStateException("Instance not created for: $name")
    }

    fun <T: Any> getInstance(name: String, type: KClass<T>): T {
        val instance = getInstance(name)
        return instance as? T ?: throw ClassCastException("Instance $name is not of type $type")
    }

    fun getAllInstances(): Map<String, Any> {
        providers.keys.forEach { name ->
            if (!instances.containsKey(name) && !resolved.contains(name)) {
                try {
                    getInstance(name)
                } catch (e: Exception) { }
            }
        }

        var changed = true
        while (changed) {
            changed = false
            providers.keys.forEach { name ->
                if (!instances.containsKey(name) && !resolved.contains(name)) {
                    try {
                        getInstance(name)
                        changed = true
                    } catch (e: Exception) { }
                }
            }
        }

        return instances.toMap()
    }

    fun clear() {
        instances.clear()
        providers.clear()
        resolved.clear()
    }
}