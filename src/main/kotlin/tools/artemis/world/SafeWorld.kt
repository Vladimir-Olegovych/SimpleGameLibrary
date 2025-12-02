package tools.artemis.world

import com.artemis.World
import com.artemis.WorldConfiguration

class SafeWorld(configuration: WorldConfiguration): World(configuration) {
    override fun delete(entityId: Int) {
        if (this.entityManager.isActive(entityId)) super.delete(entityId)
    }
}