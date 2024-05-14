package com.lesa.app.domain.model

import com.lesa.app.data.local.entities.StreamEntity

data class Stream(
    val id: Int,
    val name: String,
    val isSubscribed: Boolean,
    val topics: List<Topic>,
    val color: String?
)

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