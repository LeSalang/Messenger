package com.lesa.app.domain.model

import android.os.Parcelable
import com.lesa.app.data.local.entities.StreamEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stream(
    val id: Int,
    val name: String,
    val isSubscribed: Boolean,
    val topics: List<Topic>,
    val color: String?
) : Parcelable

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