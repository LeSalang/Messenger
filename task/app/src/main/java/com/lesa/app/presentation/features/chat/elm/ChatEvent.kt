package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Message

sealed interface ChatEvent {
    sealed interface Ui : ChatEvent {
        data class SelectEmoji(
            val messageId: Int,
            val emojiCode: String
        ) : Ui
        data class ActionButtonClicked(
            val content: String
        ) : Ui
        data class ShowEmojiPicker(
            val emojiId: Int
        ) : Ui
        data object Back : Ui
        data object Init : Ui
        data object ReloadChat : Ui
        data class MessageTextChanged(
            val text: String
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