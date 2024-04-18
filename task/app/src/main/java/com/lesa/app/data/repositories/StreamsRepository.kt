package com.lesa.app.data.repositories

import com.lesa.app.data.api.Api
import com.lesa.app.domain.model.Stream
import com.lesa.app.data.network.models.toStream
import com.lesa.app.data.network.models.toTopic

interface StreamsRepository {
    suspend fun getAllStreams() : List<Stream>
}

class StreamsRepositoryImpl(
    private val api: Api
) : StreamsRepository {
    override suspend fun getAllStreams(): List<Stream> {
        val subscribedStreams = api.getAllSubscribedStreams().streams.associateBy {
            it.id
        }
        val allStreams = api.getAllStreams().streams.map { stream ->
            val allTopicsInStreamsApiDto = api.getTopicsInStream(stream.id)
            val topicApiDtos = allTopicsInStreamsApiDto.topics
            val topics = topicApiDtos.map {
                it.toTopic(
                    color = subscribedStreams[stream.id]?.color,
                    streamId = stream.id,
                    streamName = stream.name
                )
            }
            stream.toStream(
                subscribedStreams = subscribedStreams,
                topics = topics
            )
        }
        return allStreams
    }
}