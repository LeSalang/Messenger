package com.lesa.app.model

data class Message(
    val id: Int,
    val senderName: String,
    val message: String,
    val emojiList: List<Emoji>
)
