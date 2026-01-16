package tools.artemis.world

import com.artemis.World
import com.artemis.WorldConfiguration
import tools.di.module.WorldUpdater
import java.util.concurrent.ConcurrentLinkedQueue

class ArtemisWorld(configuration: WorldConfiguration): World(configuration) {

    private val updaters = ConcurrentLinkedQueue<WorldUpdater>()

    fun addUpdater(worldUpdater: WorldUpdater){
        worldUpdater.create(this)
        updaters.add(worldUpdater)
    }

    override fun process() {
        updaters.forEach { it.update(delta) }
        super.process()
    }

    override fun delete(entityId: Int) {
        if (this.entityManager.isActive(entityId)) super.delete(entityId)
    }

    override fun dispose() {
        updaters.forEach { it.dispose() }
        updaters.clear()
        super.dispose()
    }
}