package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.Topic

sealed interface ChatEffect {
    data class ShowEmojiPicker(val messageId: Int) : ChatEffect
    data class ShowMessageContextMenu(
        val messageId: Int,
        val isOwn: Boolean
    ) : ChatEffect
    data object Back : ChatEffect
    data class EditMessage(
        val messageId: Int,
        val messageContent: String
    ) : ChatEffect
    data object EmojiError : ChatEffect
    data class MessageCopied(
        val text: String
    ) : ChatEffect
    data object MessageError : ChatEffect
    data object MessageChangeTopicError : ChatEffect
    data object MessageDeletingError : ChatEffect
    data class MessageMovedToAnotherTopic(val topicName: String) : ChatEffect
    data object ShowAttachmentsPicker : ChatEffect
    data object ClearMessageInput : ChatEffect
    data class UpdateActionButton(
        val icon: Int,
        val background: Int
    ) : ChatEffect
    data class ShowChangeTopicDialog(
        val messageId: Int,
        val stream: Stream
    ) : ChatEffect
    data class OpenTopic(
        val topic: Topic?,
        val stream: Stream
    ) : ChatEffect
}