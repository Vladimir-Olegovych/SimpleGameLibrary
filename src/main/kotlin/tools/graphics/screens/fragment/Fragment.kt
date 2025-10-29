package tools.graphics.screens.fragment

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import java.util.concurrent.Executor

abstract class Fragment: Executor {

    protected val lifecycleScope = CoroutineScope(this.asCoroutineDispatcher()  + SupervisorJob())

    fun destroyFragment() {
        lifecycleScope.cancel()
        onDestroy()
    }

    open fun onCreate(game: Game) {}
    open fun onDestroy() {}

    open fun onPause() {}
    open fun onResume() {}

    open fun onRender(deltaTime: Float) {}
    open fun onResize(width: Int, height: Int) {}

    override fun execute(runnble: Runnable) {
        Gdx.app.postRunnable { runnble.run() }
    }

    val screen = object : Screen {
        override fun show() {}
        override fun render(deltaTime: Float) { onRender(deltaTime) }
        override fun resize(width: Int, height: Int) { onResize(width, height) }
        override fun pause() { onPause() }
        override fun resume() { onResume() }
        override fun hide() {}
        override fun dispose() { destroyFragment() }
    }

}