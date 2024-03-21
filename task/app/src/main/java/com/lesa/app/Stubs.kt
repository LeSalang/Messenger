package com.lesa.app

import com.lesa.app.model.Message

val stubMessageList: MutableList<Message>
    get() = mutableListOf(
        Message(id = 3481, senderName = "Tommie Hunter", message = "corrumpit", emojiList = listOf()),
        Message(id = 1600, senderName = "Julianne Lowe", message = "consequat", emojiList = listOf()),
        Message(id = 46546, senderName = "Julianne Lowe", message = "wqeqweqeqwe", emojiList = listOf()),
        Message(id = 444, senderName = "Julianne Lowe", message = "9999999999", emojiList = listOf()),
    )

val allEmojis = listOf(
    "🐒", "🦍", "🦧", "🦝", "🦄", "🦓", "🐷", "🐏", "🐑", "🐫", "🐿", "🐊", "🦕", "🦖"
)
