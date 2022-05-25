package science.example.chat


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import science.example.chat.adapter.DialogAdapter
import science.example.chat.databinding.ActivityDialogBinding
import science.example.chat.model.App
import science.example.chat.model.DialogService
import science.example.chat.model.DialogsListener

class DialogActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDialogBinding
    private lateinit var adapter: DialogAdapter
    private val dialogService: DialogService
        get() = (applicationContext as App).dialogService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DialogAdapter()


        //отображение RV вертикальным списком
        val layoutManager = LinearLayoutManager(this)

        binding.rvMessage.layoutManager = layoutManager
        binding.rvMessage.adapter = adapter

        val itemAnimator = binding.rvMessage.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        dialogService.addListener(dialogsListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        dialogService.removeListener(dialogsListener)
    }

    private val dialogsListener: DialogsListener = {
        adapter.dialogs = it
    }


}