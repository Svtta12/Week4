package science.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import science.example.chat.adapter.UserActionListener
import science.example.chat.adapter.UsersAdapter
import science.example.chat.databinding.ActivityMainBinding
import science.example.chat.model.*


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    //отображение RV вертикальным списком
    val layoutManager = LinearLayoutManager(this)


    val lastVisibleItemPosition: Int
        get() = layoutManager.findLastVisibleItemPosition()

    private val usersService: UsersService
        get() = (applicationContext as App).usersService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //swipe
        swipeRefresh = this.findViewById(R.id.refresh)
        swipeRefresh.setOnRefreshListener {
            this.myUpdateOperation()
//            swipeRefresh.isRefreshing = false
        }

        swipeRefresh.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        adapter = UsersAdapter(object : UserActionListener {

            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                val intent = Intent(this@MainActivity, DialogActivity::class.java)
                startActivity(intent)
            }

        })

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        usersService.addListener(usersListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                swipeRefresh.isRefreshing = true
                myUpdateOperation()
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }


    private val usersListener: UsersListener = {
        adapter.users = it
    }

    private fun myUpdateOperation() {
        swipeRefresh.isRefreshing = false
    }



}


