package com.lesa.app.presentation.features.chat.elm

sealed interface ChatEffect {
    data object EmojiError : ChatEffect
}