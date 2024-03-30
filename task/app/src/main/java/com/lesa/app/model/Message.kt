package com.lesa.app.model

import com.lesa.app.chat.message.MessageView
import java.util.Date

data class Message(
    val id: Int,
    val senderName: String,
    val message: String,
    val emojiList: List<Emoji>,
    val date: Date,
    val type: MessageView.Model.Type,
)
