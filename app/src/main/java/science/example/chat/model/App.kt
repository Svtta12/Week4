package science.example.chat.model

import android.app.Application

class App: Application() {
    val usersService = UsersService()
    val dialogService = DialogService()
}