package tools.artemis.world

import com.artemis.BaseSystem
import com.artemis.World
import com.artemis.WorldConfiguration

class ArtemisWorldBuilder {
    private val systems = ArrayList<BaseSystem>()
    private val registeredObjects = ArrayList<Any>()

    fun addObject(value: Any) = apply {
        this.registeredObjects.add(value)
    }

    fun addObjects(value: Array<*>) = apply {
        value.forEach {  this.registeredObjects.add(it as Any) }
    }

    fun addSystem(system: BaseSystem) = apply {
        this.systems.add(system)
    }

    fun addSystems(systems: Array<BaseSystem>) = apply {
        this.systems.addAll(systems)
    }

    fun removeObject(value: Any) = apply {
        this.registeredObjects.remove(value)
    }

    fun removeSystem(system: BaseSystem) = apply {
        this.systems.remove(system)
    }

    fun build(): World {
        val configuration = WorldConfiguration()
        registeredObjects.forEach { registeredObject ->
            configuration.register(registeredObject)
        }
        systems.forEach { system ->
            configuration.setSystem(system)
        }
        val world = World(configuration)

        registeredObjects.clear()
        systems.clear()

        return world
    }
}