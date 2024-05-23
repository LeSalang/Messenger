package com.lesa.app.presentation.features.chat.elm

import android.net.Uri
import com.lesa.app.domain.model.MessageAnchor
import com.lesa.app.domain.model.Topic

sealed interface ChatCommand {
    data class LoadAllMessages(val topic: Topic) : ChatCommand
    data class LoadAllCachedMessages(val topic: Topic) : ChatCommand
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
    data class FetchMoreMessages(
        val topic: Topic,
        val anchor: MessageAnchor
    ) : ChatCommand
    data class UploadFile(
        val name: String,
        val uri: Uri,
    ) : ChatCommand
    data class DeleteMessage(
        val messageId: Int
    ) : ChatCommand
    data class EditMessage(
        val messageId: Int,
        val content: String
    ) : ChatCommand
    data class ChangeMessageTopic(
        val messageId: Int,
        val topicName: String
    ) : ChatCommand
}