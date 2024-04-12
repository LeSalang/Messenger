package com.lesa.app.people

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lesa.app.App
import com.lesa.app.model.User
import com.lesa.app.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val users = MutableStateFlow(emptyList<User>())

    fun getAllUsers() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                users.value = userRepository.getAllUsers()
            }
        }
    }
}

class UserViewModelFactory(
    private val context: Context
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val application = context.applicationContext as App
        val userRepository = application.appContainer.userRepository
        return UserViewModel(
            userRepository = userRepository
        ) as T
    }
}