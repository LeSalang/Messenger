package com.lesa.app.profile

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

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState.Loading)
    val state: StateFlow<ProfileScreenState>
        get() = _state.asStateFlow()

    fun getUser() {
        viewModelScope.launch {
            _state.value = ProfileScreenState.Loading
            try {
                _state.value = ProfileScreenState.DataLoaded(userRepository.getOwnUser())
            } catch (e: Exception) {
                _state.value = ProfileScreenState.Error
            }
        }
    }
}

class ProfileViewModelFactory(
    private val context: Context
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val application = context.applicationContext as App
        val userRepository = application.appContainer.userRepository
        return ProfileViewModel(
            userRepository = userRepository
        ) as T
    }
}