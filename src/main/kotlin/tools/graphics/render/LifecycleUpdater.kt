package tools.graphics.render

import kotlinx.coroutines.*
import java.util.concurrent.Executor

abstract class LifecycleUpdater(
    private var deltaTime: Float = DELTA_TIME,
    private var dispatcher: CoroutineDispatcher
) {

    protected val lifecycleScope = CoroutineScope(dispatcher)

    @Volatile private var step = (deltaTime * 1_000_000_000L).toLong()
    @Volatile private var lastTime = System.nanoTime()
    @Volatile private var timeSinceLastUpdate = 0L
    @Volatile private var isRunning = false

    private var job: Job? = null

    abstract fun create()
    abstract fun update(deltaTime: Float)
    abstract fun dispose()

    fun stop(){
        isRunning = false
        runBlocking { job?.join() }
        job = null
        lifecycleScope.cancel()
    }

    fun start() {
        if (isRunning) return
        job = lifecycleScope.launch {
            create()
            isRunning = true
            lastTime = System.nanoTime()
            while (isRunning) update()
            dispose()
            isRunning = true
        }
    }


    private fun update() {
        lastTime = System.nanoTime().also { timeSinceLastUpdate += it - lastTime }
        if (timeSinceLastUpdate < step) {
            val remain = step - timeSinceLastUpdate
            if (remain >= 1_000_000L) Thread.sleep(remain / 1_000_000L)
        } else {
            do {
                update(deltaTime)
                timeSinceLastUpdate -= step
            } while (timeSinceLastUpdate >= step)
        }
    }

    companion object {
        const val DELTA_TIME = 1F / 60F
    }
}