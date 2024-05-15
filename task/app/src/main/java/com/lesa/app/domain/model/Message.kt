package com.lesa.app.domain.model

import java.util.Date

data class Message(
    val id: Int,
    val senderAvatar: String?,
    val content: String,
    val senderName: String,
    val reactions: Map<String, Emoji>,
    val date: Date,
    val topic: String,
    val isOwn: Boolean
)