package tools.graphics.input

import com.badlogic.gdx.InputProcessor

abstract class SwitchInputProcessor : InputProcessor {
    private var enabled = true

    fun isEnabled(): Boolean = enabled

    fun setEnabled(value: Boolean) {
        if (enabled == value) return
        enabled = value
        if (value) onEnable() else onDisable()
    }

    open fun onEnable(){}
    open fun onDisable(){}

    open fun swKeyDown(keycode: Int): Boolean = false
    open fun swKeyUp(keycode: Int): Boolean = false
    open fun swKeyTyped(character: Char): Boolean = false
    open fun swTouchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    open fun swTouchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    open fun swTouchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    open fun swTouchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
    open fun swMouseMoved(screenX: Int, screenY: Int): Boolean = false
    open fun swScrolled(amountX: Float, amountY: Float): Boolean = false

    override fun keyDown(keycode: Int): Boolean = enabled && swKeyDown(keycode)
    override fun keyUp(keycode: Int): Boolean = enabled && swKeyUp(keycode)
    override fun keyTyped(character: Char): Boolean = enabled && swKeyTyped(character)
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
        enabled && swTouchDown(screenX, screenY, pointer, button)
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
        enabled && swTouchUp(screenX, screenY, pointer, button)
    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
        enabled && swTouchCancelled(screenX, screenY, pointer, button)
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean =
        enabled && swTouchDragged(screenX, screenY, pointer)
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = enabled && swMouseMoved(screenX, screenY)
    override fun scrolled(amountX: Float, amountY: Float): Boolean = enabled && swScrolled(amountX, amountY)
}