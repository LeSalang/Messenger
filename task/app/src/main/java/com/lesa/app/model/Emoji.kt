package com.lesa.app.model

data class Emoji(
    val emojiCode: String,
    val userIds: Set<Int>,
    val count: Int = 1,
)
