package com.lesa.app.presentation.features.chat.elm

sealed interface ChatEffect {
    data class ShowEmojiPicker(val emojiId: Int) : ChatEffect
    data object Back : ChatEffect
    data object EmojiError : ChatEffect
    data object ShowAttachmentsPicker : ChatEffect
    data object ClearMessageInput : ChatEffect
    data class UpdateActionButton(
        val icon: Int,
        val background: Int
    ) : ChatEffect
}