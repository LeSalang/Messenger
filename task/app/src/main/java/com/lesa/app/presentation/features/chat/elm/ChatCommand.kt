package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.presentation.features.chat.models.TopicUi

sealed interface ChatCommand {
    data class LoadAllMessages(val topicUi: TopicUi) : ChatCommand
    data class SendMessage(
        val content: String,
        val topicUi: TopicUi
    ) : ChatCommand
    data class SelectEmoji(
        val messageId: Int,
        val emojiCode: String
    ) : ChatCommand
}