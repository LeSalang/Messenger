package com.lesa.app.presentation.features.chat.models

import java.util.Date

data class MessageUi(
    val id: Int,
    val avatar: String?,
    val content: String,
    val senderName: String,
    val reactions: Map<String, EmojiUi>,
    val date: Date,
    val topic: String,
    val isOwn: Boolean
)