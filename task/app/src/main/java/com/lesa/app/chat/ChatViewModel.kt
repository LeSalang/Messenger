package com.lesa.app.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lesa.app.App
import com.lesa.app.model.Topic
import com.lesa.app.repositories.MessagesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val messagesRepository: MessagesRepository
) : ViewModel() {
    private val _state: MutableStateFlow<ChatScreenState> =
        MutableStateFlow(ChatScreenState.Loading)
    val state: StateFlow<ChatScreenState>
        get() = _state.asStateFlow()

    var topic: Topic? = null

    fun loadChannels() {
        viewModelScope.launch {
            _state.value = ChatScreenState.Loading
            try {
                _state.value = ChatScreenState.DataLoaded(
                    list = messagesRepository.getAllMessagesInStream(
                        streamName = topic!!.streamName
                    )
                )
            } catch (e: Exception) {
                _state.value = ChatScreenState.Error
            }
        }
    }
}

class ChatViewModelFactory(
    private val context: Context,
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val application = context.applicationContext as App
        val messagesRepository = application.appContainer.messagesRepository
        return ChatViewModel(
            messagesRepository = messagesRepository
        ) as T
    }
}