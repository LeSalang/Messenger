package com.lesa.app.presentation.features.chat.elm

import android.net.Uri
import com.lesa.app.domain.model.MessageAnchor

sealed interface ChatCommand {
    // commands for message list:
    data class LoadAllMessages(
        val topicName: String?, val streamName: String
    ) : ChatCommand

    data class LoadAllCachedMessages(
        val topicName: String?, val streamName: String
    ) : ChatCommand

    data class FetchMoreMessages(
        val topicName: String?, val streamName: String, val anchor: MessageAnchor
    ) : ChatCommand

    // commands for one message:
    data class SendMessage(
        val content: String, val topicName: String?, val streamId: Int
    ) : ChatCommand

    data class EditMessage(
        val messageId: Int, val content: String
    ) : ChatCommand

    data class DeleteMessage(
        val messageId: Int
    ) : ChatCommand

    data class ChangeMessageTopic(
        val messageId: Int, val topicName: String
    ) : ChatCommand

    data class UploadFile(
        val name: String, val uri: Uri,
    ) : ChatCommand

    // commands for reactions:
    data class AddReaction(
        val messageId: Int, val emojiName: String
    ) : ChatCommand

    data class RemoveReaction(
        val messageId: Int, val emojiName: String
    ) : ChatCommand
}