package tools.graphics.screens.dialogs

class DialogManager {

    private val dialogs = ArrayList<Dialog>()

    fun addDialog(dialog: Dialog){
        dialog.onCreate()
        dialogs.add(dialog)
    }

    fun removeDialog(dialog: Dialog){
        dialogs.remove(dialog)
    }

    fun clear(){
        ArrayList(dialogs).forEach { it.dismiss() }
        dialogs.clear()
    }

    fun onDestroy() {
        ArrayList(dialogs).forEach { it.dismiss() }
        dialogs.clear()
    }

    fun onPause() {
        dialogs.forEach { it.onPause() }
    }

    fun onResume() {
        dialogs.forEach { it.onResume() }
    }

    fun onRender(deltaTime: Float) {
        dialogs.forEach { it.onRender(deltaTime) }
    }

    fun onResize(width: Int, height: Int) {
        dialogs.forEach { it.onResize(width, height) }
    }

}