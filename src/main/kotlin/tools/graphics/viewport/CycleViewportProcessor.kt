package tools.graphics.viewport

import com.badlogic.gdx.utils.viewport.Viewport

class CycleViewportProcessor {

    private var viewports = ArrayList<Viewport>()

    fun addViewport(viewport: Viewport) = apply {
        viewports.add(viewport)
    }

    fun removeViewport(viewport: Viewport) = apply {
        viewports.remove(viewport)
    }

    fun clear(){
        viewports.clear()
    }

    fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
        viewports.forEach { it.update(screenWidth, screenHeight, centerCamera) }
    }
}