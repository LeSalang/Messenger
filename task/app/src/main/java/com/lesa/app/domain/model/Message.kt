package com.lesa.app.domain.model

import java.util.Date

data class Message(
    val id: Int,
    val avatar: String?,
    val content: String,
    val senderName: String,
    val reactions: Map<String, Emoji>,
    val date: Date,
    val topic: String,
    val isOwn: Boolean
)

data class SentMessage(
    val content: String,
)