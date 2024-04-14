package com.lesa.app.repositories

import com.lesa.app.api.Api
import com.lesa.app.model.Stream
import com.lesa.app.model.api_models.toStream
import com.lesa.app.model.api_models.toTopic

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
        val allStreams = api.getAllStreams().streams.map {stream ->
            val topics = api.getTopicsInStream(stream.id).topics.map {
                it.toTopic(subscribedStreams[stream.id]?.color)
            }
            stream.toStream(
                subscribedStreams = subscribedStreams,
                topics = topics
            )
        }
        return allStreams
    }
}