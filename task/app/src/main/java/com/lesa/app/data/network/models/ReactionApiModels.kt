package com.lesa.app.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionApiDto(
    @SerialName("emoji_code")
    val emojiCode: String,
    @SerialName("emoji_name")
    val emojiName: String,
    @SerialName("user_id")
    val userId: Int
)