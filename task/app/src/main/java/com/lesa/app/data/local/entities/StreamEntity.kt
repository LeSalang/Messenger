package com.lesa.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lesa.app.data.network.models.FALLBACK_STREAM_COLOR
import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.Topic

@Entity(tableName = "streams")
data class StreamEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("color") val color: String?,
    @ColumnInfo("isSubscribed") val isSubscribed: Boolean,
    @ColumnInfo("topics") val topics: List<String>
)

fun StreamEntity.toStream() : Stream {
    return Stream(
        id = id,
        name = name,
        isSubscribed = isSubscribed,
        color = color,
        topics = topics.map { topicName ->
            Topic(
                name = topicName,
                color = color ?: FALLBACK_STREAM_COLOR,
                streamName = name,
                streamId = id
            )
        }
    )
}

fun Stream.toStreamEntity() : StreamEntity {
    return StreamEntity(
        id = id,
        name = name,
        color = color,
        isSubscribed = isSubscribed,
        topics = topics.map { topic ->
            topic.name
        }
    )
}