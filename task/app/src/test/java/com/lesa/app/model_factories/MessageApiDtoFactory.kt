package com.lesa.app.model_factories

import com.lesa.app.data.network.models.MessageApiDto
import com.lesa.app.data.network.models.ReactionApiDto

object MessageApiDtoFactory {
    fun create(
        avatar: String? = "https://avatar",
        content: String = "content",
        id: Int = 1,
        reactions: List<ReactionApiDto> = listOf(
            ReactionApiDto(
                emojiCode = "emojiCode1",
                emojiName = "emojiName1",
                userId = 10
            ),
            ReactionApiDto(
                emojiCode = "emojiCode2",
                emojiName = "emojiName2",
                userId = 10
            ),
            ReactionApiDto(
                emojiCode = "emojiCode2",
                emojiName = "emojiName2",
                userId = 20
            )
        ),
        senderId: Int = 10,
        senderFullName: String = "Name Surnamov",
        topic: String = "topic",
        timestamp: Int = 1000000000
    ): MessageApiDto {
        return MessageApiDto(
            avatar = avatar,
            content = content,
            id = id,
            reactions = reactions,
            senderId = senderId,
            senderFullName = senderFullName,
            topic = topic,
            timestampSecs = timestamp
        )
    }
}