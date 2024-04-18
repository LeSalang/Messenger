package com.lesa.app.domain.model

data class Emoji(
    val emojiCode: String,
    val emojiName: String,
    val isOwn: Boolean,
    val count: Int = 1,
)
