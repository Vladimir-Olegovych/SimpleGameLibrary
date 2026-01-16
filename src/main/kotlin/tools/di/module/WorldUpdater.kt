package tools.di.module

import com.artemis.World

interface WorldUpdater {
    fun create(artemisWorld: World) {}
    fun update(deltaTime: Float) {}
    fun dispose() {}
}