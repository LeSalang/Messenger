package com.lesa.app.model_factories

import com.lesa.app.data.local.entities.MessageEntity
import com.lesa.app.data.local.entities.ReactionEntity

object MessageEntityFactory {
    fun create(
        id: Int = 1,
        content: String = "content",
        senderName: String = "Name Surnamov",
        senderAvatar: String? = "https://avatar",
        timestamp: Long = 1000000000,
        topic: String = "topic",
        reactions: List<ReactionEntity> = listOf(
            ReactionEntity(
                emojiCode = "emojiCode1",
                emojiName = "emojiName1",
                count = 1,
                isOwn = false
            ),
            ReactionEntity(
                emojiCode = "emojiCode2",
                emojiName = "emojiName2",
                count = 2,
                isOwn = true
            )
        ),
        isOwn: Boolean = false
    ): MessageEntity {
        return MessageEntity(
            id = id,
            content = content,
            senderName = senderName,
            senderAvatar = senderAvatar,
            timestamp = timestamp,
            topic = topic,
            reactions = reactions,
            isOwn = isOwn
        )
    }
}