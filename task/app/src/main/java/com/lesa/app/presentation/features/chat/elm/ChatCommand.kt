package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Topic

sealed interface ChatCommand {
    data class LoadAllMessages(val topic: Topic) : ChatCommand
    data class SendMessage(
        val content: String,
        val topic: Topic
    ) : ChatCommand
    data class AddReaction(
        val messageId: Int,
        val emojiName: String
    ) : ChatCommand
    data class RemoveReaction(
        val messageId: Int,
        val emojiName: String
    ) : ChatCommand
}