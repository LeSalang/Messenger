package com.lesa.app.data.network.models

import com.lesa.app.domain.model.Emoji
import com.lesa.app.domain.model.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class AllMessagesApiDto(
    @SerialName("messages")
    val messages: List<MessageApiDto>
)

@Serializable
data class MessageResponseApiDto(
    @SerialName("message")
    val message: MessageApiDto
)

@Serializable
data class SendMessageResponseApiDto(
    @SerialName("id") val id: Int
)

@Serializable
data class MessageApiDto(
    @SerialName("avatar_url")
    val avatar: String?,
    @SerialName("content")
    val content: String,
    @SerialName("id")
    val id: Int,
    @SerialName("reactions")
    val reactions: List<ReactionApiDto>,
    @SerialName("sender_id")
    val senderId: Int,
    @SerialName("sender_full_name")
    val senderFullName: String,
    @SerialName("subject")
    val topic: String,
    @SerialName("timestamp")
    val timestampSecs: Int
)

fun MessageApiDto.toMessage(ownId: Int) : Message {
    return Message(
        id = id,
        senderAvatar = avatar,
        content = content,
        senderName = senderFullName,
        reactions = reactions
            .groupingBy { it.emojiCode }
            .aggregate { emojiCode, emoji: Emoji?, apiDto, _ ->
                return@aggregate Emoji(
                    emojiCode = emojiCode,
                    emojiName = apiDto.emojiName,
                    isOwn = (apiDto.userId == ownId) || (emoji?.isOwn ?: false),
                    count = 1 + (emoji?.count ?: 0)
                )
            },
        date = Date(timestampSecs.toLong() * 1000),
        topic = topic,
        isOwn = senderId == ownId
    )
}