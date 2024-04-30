package com.lesa.app.presentation.features.chat.elm

sealed interface ChatEffect {
    data class ShowEmojiPicker(val emojiId: Int) : ChatEffect
    data object Back : ChatEffect
    data object EmojiError : ChatEffect
}