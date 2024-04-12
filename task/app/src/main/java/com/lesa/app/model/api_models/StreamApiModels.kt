package com.lesa.app.model.api_models

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
    val name: String
)

@Serializable
data class TopicApiDto(
    @SerialName("name")
    val name: String
)