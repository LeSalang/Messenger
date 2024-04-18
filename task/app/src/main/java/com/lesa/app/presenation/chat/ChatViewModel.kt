package com.lesa.app.presenation.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lesa.app.App
import com.lesa.app.domain.model.Message
import com.lesa.app.domain.model.Topic
import com.lesa.app.domain.model.emojiSetCNCS
import com.lesa.app.data.repositories.MessagesRepository
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

    fun loadMessages() {
        viewModelScope.launch {
            _state.value = ChatScreenState.Loading
            try {
                _state.value = ChatScreenState.DataLoaded(
                    list = messagesRepository.getAllMessagesInStream(
                        streamName = topic!!.streamName, topicName = topic!!.name
                    )
                )
            } catch (e: Exception) {
                _state.value = ChatScreenState.Error
            }
        }
    }

    fun sendMessages(
        content: String,
    ) {
        viewModelScope.launch {
            val id = messagesRepository.sendMessage(
                content = content,
                topicName = topic!!.name,
                streamId = topic!!.streamId
            )
            loadMessage(id)
        }
    }

    fun onSelectEmoji( // TODO: crash on fast clicks
        message: Message,
        emojiCode: String,
    ) {
        val emoji = message.reactions[emojiCode]
        viewModelScope.launch {
            if (emoji != null) {
                val emojiName = emoji.emojiName
                if (emoji.isOwn) {
                    messagesRepository.deleteReaction(
                        messageId = message.id,
                        emojiName = emojiName
                    )
                } else {
                    messagesRepository.addReaction(
                        messageId = message.id,
                        emojiName = emojiName
                    )
                }
            } else {
                val emojiName = emojiSetCNCS.firstOrNull {
                    it.code == emojiCode
                }?.name ?: return@launch
                messagesRepository.addReaction(
                    messageId = message.id,
                    emojiName = emojiName
                )
            }
            updateMessage(message.id)
        }
    }

    private suspend fun updateMessage(id: Int) {
        val newMessage = messagesRepository.getMessage(id)
        val index = state.value.messages.indexOfFirst {
            it.id == id
        }
        val messages = state.value.messages.toMutableList()
        messages[index] = newMessage
        _state.value = ChatScreenState.DataLoaded(
            list = messages
        )
    }

    private suspend fun loadMessage(id: Int) {
        val newMessage = messagesRepository.getMessage(id)
        val messages = state.value.messages.toMutableList()
        messages.add(newMessage)
        _state.value = ChatScreenState.DataLoaded(
            list = messages
        )
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