package com.lesa.app.model_factories

import com.lesa.app.domain.model.Emoji
import com.lesa.app.domain.model.Message
import java.util.Date

object MessageFactory {
    fun create(
        id: Int = 1,
        senderAvatar: String = "senderAvatar",
        content: String = "content",
        senderName: String = "senderName",
        reactions: Map<String, Emoji> = mapOf(
            "emojiCode1" to Emoji(
                emojiCode = "emojiCode1",
                emojiName = "emojiName1",
                isOwn = false,
                count = 1
            ),
            "emojiCode2" to Emoji(
                emojiCode = "emojiCode2",
                emojiName = "emojiName2",
                isOwn = true,
                count = 2
            )
        ),
        date: Date = Date(1_000_000_000),
        topic: String = "topic",
        isOwn: Boolean = false
    ): Message {
        return Message(
            id = id,
            senderAvatar = senderAvatar,
            content = content,
            senderName = senderName,
            reactions = reactions,
            date = date,
            topic = topic,
            isOwn = isOwn
        )
    }
}