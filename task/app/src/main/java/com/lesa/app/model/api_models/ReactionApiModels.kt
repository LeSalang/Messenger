package com.lesa.app.model.api_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionApiDto(
    @SerialName("emoji_code")
    val emojiCode: String,
    @SerialName("user_id")
    val userId: Int
)