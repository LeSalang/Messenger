package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Message

sealed interface ChatEvent {
    sealed interface Ui : ChatEvent {
        data object Init : Ui
        data object ReloadChat : Ui
        data class SendMessage(
            val content: String
        ) : Ui
        data class SelectEmoji(
            val messageId: Int,
            val emojiCode: String
        ) : Ui
    }

    sealed interface Internal : ChatEvent {
        data class AllMessagesLoaded(val messages: List<Message>) : Internal
        data class MessageSent(val sentMessage: Message) : Internal
        data class MessageUpdated(val updatedMessage: Message) : Internal
        data object Error : Internal
        data object ErrorMessage : Internal
    }
}