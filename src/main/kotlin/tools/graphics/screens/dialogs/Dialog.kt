package tools.graphics.screens.dialogs

abstract class Dialog {

    protected var dialogManager: DialogManager? = null
    private var showed = false

    fun isShowed(): Boolean = showed

    open fun onCreate() {}
    open fun onDestroy() {}

    open fun onPause() {}
    open fun onResume() {}

    open fun onRender(deltaTime: Float) {}
    open fun onResize(width: Int, height: Int) {}

    fun dismiss(){
        if (dialogManager == null) return
        dialogManager?.removeDialog(this)
        onDestroy()
        dialogManager = null
        showed = false
    }

    fun show(dialogManager: DialogManager){
        if (this.dialogManager != null) return
        this.dialogManager = dialogManager
        dialogManager.addDialog(this)
        showed = true
    }

}