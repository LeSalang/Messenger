package com.lesa.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lesa.app.domain.model.Message
import java.util.Date

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("sender_name") val senderName: String,
    @ColumnInfo("sender_avatar") val senderAvatar: String?,
    @ColumnInfo("timestamp") val timestamp: Long,
    @ColumnInfo("subject") val topic: String,
    @ColumnInfo("reactions") val reactions: List<ReactionEntity>,
    @ColumnInfo("isOwn") val isOwn: Boolean
)

fun MessageEntity.toMessage(): Message {
    return Message(
        id = id,
        senderAvatar = senderAvatar,
        content = content,
        senderName = senderName,
        reactions = reactions.associate {
            it.emojiCode to it.toEmoji()
        }
        ,
        date = Date(timestamp * 1000),
        topic = topic,
        isOwn = isOwn
    )
}