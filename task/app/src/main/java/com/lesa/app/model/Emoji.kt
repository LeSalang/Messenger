package com.lesa.app.model

data class Emoji(
    val emojiCode: String,
    val isOwn: Boolean,
    val count: Int = 1,
)
