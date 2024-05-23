package com.lesa.app.presentation.features.chat.elm

import android.net.Uri
import com.lesa.app.domain.model.Message
import com.lesa.app.presentation.features.chat.message_context_menu.MessageContextMenuAction

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
            val messageId: Int
        ) : Ui
        data class ShowContextMessageBottomSheet(
            val messageId: Int
        ) : Ui
        data object Back : Ui
        data object Init : Ui
        data object ReloadChat : Ui
        data class MessageTextChanged(
            val text: String
        ) : Ui
        data object FetchMoreMessages : Ui
        data class UploadFile(
            val name: String,
            val uri: Uri
        ) : Ui
        data class SelectMenuAction(
            val messageId: Int,
            val action: MessageContextMenuAction
        ) : Ui
        data class EditMessage(
            val messageId: Int,
            val messageContent: String
        ) : Ui
        data class ChangeMessageTopic(
            val messageId: Int,
            val topicName: String
        ) : Ui
    }

    sealed interface Internal : ChatEvent {
        data class AllCachedMessagesLoaded(val messages: List<Message>) : Internal
        data class AllMessagesLoaded(val messages: List<Message>) : Internal
        data class FileUploaded(val uri: String) : Internal
        data class MessageDeleted(val messageId: Int) : Internal
        data class MessageSent(val sentMessage: Message) : Internal
        data class MessageUpdated(val updatedMessage: Message) : Internal
        data class OldMessagesLoaded(val messages: List<Message>) : Internal
        data object Error : Internal
        data object ErrorCached : Internal
        data object ErrorDeleteEmoji : Internal
        data object ErrorEmoji : Internal
        data object ErrorMessage : Internal
        data object ErrorMessageChangeTopic : Internal
        data object ErrorEditMessage : Internal
    }
}