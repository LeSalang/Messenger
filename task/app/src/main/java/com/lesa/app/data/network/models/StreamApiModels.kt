package com.lesa.app.data.network.models

import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.Topic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllStreamsApiDto(
    @SerialName("streams")
    val streams: List<StreamApiDto>
)

@Serializable
data class AllSubscribedStreamsApiDto(
    @SerialName("subscriptions")
    val streams: List<StreamApiDto>
)

@Serializable
data class StreamApiDto(
    @SerialName("stream_id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("color")
    val color: String? = null
)

@Serializable
data class AllTopicsInStreamApiDto(
    val topics: List<TopicApiDto>
)

@Serializable
data class TopicApiDto(
    @SerialName("name")
    val name: String
)

fun StreamApiDto.toStream(
    subscribedStreams: Map<Int, StreamApiDto>,
    topics: List<Topic>
) : Stream {
    return Stream(
        id = id,
        name = name,
        isSubscribed = subscribedStreams.containsKey(id),
        topics = topics,
        color = subscribedStreams[id]?.color ?: FALLBACK_STREAM_COLOR
    )
}

fun TopicApiDto.toTopic(
    color: String?,
    streamId: Int,
    streamName: String
) : Topic {
    return Topic(
        name = name,
        color = color ?: FALLBACK_STREAM_COLOR,
        streamName = streamName,
        streamId = streamId
    )
}

const val FALLBACK_STREAM_COLOR = "#464646"