package com.lesa.app.data.local.entities

import com.lesa.app.domain.model.Emoji
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionEntity(
    @SerialName("emoji_code") val emojiCode: String,
    @SerialName("emoji_name") val emojiName: String,
    @SerialName("count") val count: Int,
    @SerialName("is_own") val isOwn: Boolean
)

fun ReactionEntity.toEmoji() : Emoji {
    return Emoji(
        emojiCode = emojiCode,
        emojiName = emojiName,
        isOwn = isOwn,
        count = count
    )
}

fun Emoji.toReactionEntity() : ReactionEntity {
    return ReactionEntity(
        emojiCode = emojiCode,
        emojiName = emojiName,
        count = count,
        isOwn = isOwn
    )
}