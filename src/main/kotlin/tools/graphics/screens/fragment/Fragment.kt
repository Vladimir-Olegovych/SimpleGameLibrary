package tools.graphics.screens.fragment

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import tools.graphics.screens.dialogs.DialogManager
import java.util.concurrent.Executor

abstract class Fragment: Executor {

    protected val dialogManager = DialogManager()
    protected val lifecycleScope = CoroutineScope(this.asCoroutineDispatcher()  + SupervisorJob())

    fun destroyFragment() {
        onDestroy()
        dialogManager.onDestroy()
        dialogManager.clear()
        lifecycleScope.cancel()
    }

    open fun onCreate() {}
    open fun onDestroy() {}

    open fun onPause() {}
    open fun onResume() {}

    open fun onRender(deltaTime: Float) {}
    open fun onResize(width: Int, height: Int) {}


    override fun execute(runnble: Runnable) {
        Gdx.app.postRunnable { runnble.run() }
    }

    val screen = object : Screen {
        override fun hide() {}
        override fun show() {}
        override fun render(deltaTime: Float) {
            onRender(deltaTime)
            dialogManager.onRender(deltaTime)
        }
        override fun resize(width: Int, height: Int) {
            onResize(width, height)
            dialogManager.onResize(width, height)
        }
        override fun pause() {
            onPause()
            dialogManager.onPause()
        }
        override fun resume() {
            onResume()
            dialogManager.onResume()
        }
        override fun dispose() {
            destroyFragment()
        }
    }

}