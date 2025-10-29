package tools.graphics.input

import com.badlogic.gdx.InputProcessor

class CycleInputProcessor: InputProcessor {

    private var processors = ArrayList<InputProcessor>()

    fun addProcessor(processor: InputProcessor) = apply {
        processors.add(processor)
    }

    fun removeProcessor(processor: InputProcessor) = apply {
        processors.remove(processor)
    }

    fun clear(){
        processors.clear()
    }

    override fun keyDown(keycode: Int): Boolean {
        processors.forEach { it.keyDown(keycode) }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        processors.forEach { it.keyUp(keycode) }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        processors.forEach { it.keyTyped(character) }
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        processors.forEach { it.touchDown(screenX, screenY, pointer, button) }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        processors.forEach { it.touchUp(screenX, screenY, pointer, button) }
        return false
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        processors.forEach { it.touchCancelled(screenX, screenY, pointer, button) }
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        processors.forEach { it.touchDragged(screenX, screenY, pointer) }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        processors.forEach { it.mouseMoved(screenX, screenY) }
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        processors.forEach { it.scrolled(amountX, amountY) }
        return false
    }
}