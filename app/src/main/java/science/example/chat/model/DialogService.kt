package science.example.chat.model


import science.example.chat.objec.CompanionObject.MESSAGE
import science.example.chat.objec.CompanionObject.TIME


typealias DialogsListener = (dialogs: List<Dialog>) -> Unit

class DialogService {

    private var dialogs: MutableList<Dialog> = mutableListOf<Dialog>()
    private val listeners: MutableSet<DialogsListener> = mutableSetOf<DialogsListener>()

    init {
        dialogs = (0..3).map {
            Dialog(
                message2 = MESSAGE.random(),
                message1 = MESSAGE.random(),
                time = TIME[it % TIME.size],
                time2 = TIME[1+(it % TIME.size)])
        }.toMutableList()

    }

    fun getDialogs(): List<Dialog> {
        return dialogs
    }

    //добавить
    fun addListener(listener: DialogsListener) {
        listeners.add(listener)
        listener.invoke(dialogs)
    }


    //удалять
    fun removeListener(listener: DialogsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges(){
        listeners.forEach { it.invoke(dialogs) }
    }

}