package com.lesa.app.presentation.features.chat.elm

sealed interface ChatEffect {
    data object EmojiError : ChatEffect
    data class ShowEmojiPicker(val emojiId: Int) : ChatEffect
}