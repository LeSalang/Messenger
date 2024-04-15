package com.lesa.app.chat

import com.lesa.app.model.Message

sealed interface ChatScreenState {
    object Loading : ChatScreenState

    object Error : ChatScreenState

    class DataLoaded(
        val list: List<Message>
    ) : ChatScreenState
}

val ChatScreenState.messages: List<Message>
    get() = when (this) {
        is ChatScreenState.DataLoaded -> this.list
        ChatScreenState.Error -> emptyList()
        ChatScreenState.Loading -> emptyList()
    }