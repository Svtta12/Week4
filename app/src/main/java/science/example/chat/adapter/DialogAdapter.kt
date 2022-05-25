package science.example.chat.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import science.example.chat.databinding.ItemDialogsBinding
import science.example.chat.model.Dialog

class DialogAdapter: RecyclerView.Adapter<DialogAdapter.DialogsViewHolder>() {

    var dialogs: List<Dialog> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDialogsBinding.inflate(inflater, parent, false)
        return DialogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DialogAdapter.DialogsViewHolder, position: Int) {
        val dialog = dialogs[position]
        with(holder.binding) {
            tvTime.text = dialog.time
            tvBotMessage.text = dialog.message2
            tvMessage.text = dialog.message1
            tvTime2.text = dialog.time2
        }

    }
    override fun getItemCount(): Int = dialogs.size

    class DialogsViewHolder(val binding: ItemDialogsBinding) : RecyclerView.ViewHolder(binding.root)
}
