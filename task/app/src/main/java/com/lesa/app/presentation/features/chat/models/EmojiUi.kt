package com.lesa.app.presentation.features.chat.models

data class EmojiUi(
    val emojiCode: String,
    val emojiName: String,
    val isOwn: Boolean,
    val count: Int = 1
)