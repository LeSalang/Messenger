package com.lesa.app.data.repositories

import com.lesa.app.data.network.Api
import com.lesa.app.data.network.models.toStream
import com.lesa.app.data.network.models.toTopic
import com.lesa.app.domain.model.Stream
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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
        val allStreams = coroutineScope {
            api.getAllStreams().streams.map { stream ->
                val deferredTopics = async {
                    api.getTopicsInStream(stream.id)
                }
                return@map stream to deferredTopics
            }.map {
                val (stream, deferredTopics) = it
                val topicApiDtos = deferredTopics.await()
                val topics = topicApiDtos.topics.map {
                    it.toTopic(
                        color = subscribedStreams[stream.id]?.color,
                        streamId = stream.id,
                        streamName = stream.name
                    )
                }
                stream.toStream(
                    subscribedStreams = subscribedStreams, topics = topics
                )
            }
        }
        return allStreams
    }
}