package com.lesa.app.people

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lesa.app.App
import com.lesa.app.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PeopleViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _state: MutableStateFlow<PeopleScreenState> =
        MutableStateFlow(PeopleScreenState.Loading)
    val state: StateFlow<PeopleScreenState>
        get() = _state.asStateFlow()

    fun getAllUsers() {
        viewModelScope.launch {
            _state.value = PeopleScreenState.Loading
            try {
                _state.value = PeopleScreenState.DataLoaded(userRepository.getAllUsers())
            } catch (e: Exception) {
                _state.value = PeopleScreenState.Error
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
        return PeopleViewModel(
            userRepository = userRepository
        ) as T
    }
}