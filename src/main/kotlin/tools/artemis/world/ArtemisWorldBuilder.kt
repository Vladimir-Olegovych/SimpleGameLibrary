package tools.artemis.world

import alexey.tools.server.world.OptimizedInvocationStrategy
import com.artemis.BaseSystem
import com.artemis.World
import com.artemis.WorldConfiguration
import tools.di.ArtemisDI
import tools.di.module.ArtemisModule
import tools.di.module.WorldUpdater

class ArtemisWorldBuilder {
    private val artemisDI = ArtemisDI()
    private val modules = ArrayList<ArtemisModule>()
    private val systems = ArrayList<BaseSystem>()

    fun addModules(vararg modules: ArtemisModule) = apply {
        this.modules.addAll(modules)
    }

    fun addSystems(vararg systems: BaseSystem) = apply {
        this.systems.addAll(systems)
    }

    fun build(): World {
        val configuration = WorldConfiguration()

        systems.forEach { system ->
            artemisDI.addInstance(system)
            configuration.setSystem(system)
        }

        artemisDI.start(modules)
        val instances = artemisDI.getAllInstances()
        instances.forEach { (key, value) -> configuration.register(key, value) }
        configuration.isAlwaysDelayComponentRemoval = false
        configuration.setInvocationStrategy(OptimizedInvocationStrategy())
        val world = ArtemisWorld(configuration)

        for ((_, instance) in instances) {
            if (instance is BaseSystem) continue
            world.inject(instance)
            if (instance is WorldUpdater) world.addUpdater(instance)
        }

        artemisDI.clear()
        modules.clear()
        systems.clear()

        return world
    }
}