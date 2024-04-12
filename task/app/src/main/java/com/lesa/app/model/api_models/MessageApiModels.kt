package com.lesa.app.model.api_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllMessagesApiDto(
    @SerialName("messages")
    val messages: List<MessageApiDto>
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
    @SerialName("timestamp")
    val timestamp: Int
)