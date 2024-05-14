package com.lesa.app.domain.model

import com.lesa.app.data.local.entities.MessageEntity
import com.lesa.app.data.local.entities.toReactionEntity
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

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        content = content,
        senderName = senderName,
        senderAvatar = senderAvatar,
        timestamp = date.time / 1000,
        topic = topic,
        reactions = reactions.map { it.value.toReactionEntity() },
        isOwn = isOwn
    )
}

data class SentMessage(
    val content: String,
)