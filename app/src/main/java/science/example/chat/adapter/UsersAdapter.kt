package science.example.chat.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import science.example.chat.R
import science.example.chat.databinding.ItemChatBinding
import science.example.chat.model.User
import java.nio.file.Files.size

interface UserActionListener {
    //удалить пользователя
    fun onUserDelete(user: User)
    //нажатие на какой-то элемент списка
    fun onUserDetails(user: User)
}

class UsersDiffCallback(
    private val oldList: List<User>,
    private val newList: List<User>
): DiffUtil.Callback() {

    //возвращает длину старого списка
    override fun getOldListSize(): Int = oldList.size
    //возвращает длину старого списка
    override fun getNewListSize(): Int = newList.size

    //сравнение старого и нового списка по id (т.е хранится ли один и тот же элемент в этих списках)
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.id == newUser.id
    }

    //сравниваем содержимое элементов и возвращаем false, если она разное
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser == newUser
    }

}



class UsersAdapter(private val actionListener: UserActionListener) :
    RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(), View.OnClickListener {

    var users: List<User> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            val diffCallBack = UsersDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }


    override fun onClick(v: View) {
        val user = v.tag as User
        when (v.id){
            R.id.moreImageViewButton -> {
                showPopupMenu(v)
            }
            else -> {
                actionListener.onUserDetails(user)
            }
        }
    }

    //возвращает количество элементов в списке
    override fun getItemCount(): Int = users.size

    //используется когда RV хочет создать новый элемент списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(inflater, parent, false)

        //слушатели на нажатие на элемент списка и на кнопку more
        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding) {

            //если пользователь нажал на сам элемент списка
            holder.itemView.tag = user
            //если пользователь нажал на кнопку more
            moreImageViewButton.tag = user

            userNameTextView.text = user.name
            userMessageTextView.text = user.company
            userUnreadMessages.text = user.unread.toString()


            if (user.photo.isNotBlank()) {
                Glide.with(photoImageView.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_users_avatar)
                    .error(R.drawable.ic_users_avatar)
                    .into(photoImageView)
            }
            else {
                photoImageView.setImageResource(R.drawable.ic_users_avatar)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val user = view.tag as User

        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_REMOVE -> {
                    actionListener.onUserDelete(user)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }


    class UsersViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val ID_REMOVE = 1
    }
}