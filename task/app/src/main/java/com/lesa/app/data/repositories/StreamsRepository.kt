package com.lesa.app.data.repositories

import com.lesa.app.data.local.dao.StreamDao
import com.lesa.app.data.local.entities.toStream
import com.lesa.app.data.network.Api
import com.lesa.app.data.network.models.SubscriptionsQuery
import com.lesa.app.data.network.models.toStream
import com.lesa.app.data.network.models.toTopic
import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.toStreamEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface StreamsRepository {
    suspend fun getAllStreams() : List<Stream>
    suspend fun getCachedStreams() : List<Stream>
    suspend fun createStream(streamName: String) : List<Stream>
}

class StreamsRepositoryImpl @Inject constructor(
    private val api: Api,
    private val dao: StreamDao
) : StreamsRepository {
    override suspend fun getAllStreams(): List<Stream> {
        val subscribedStreams = api.getAllSubscribedStreams().streams.associateBy {
            it.id
        }
        val allStreams = coroutineScope {
            val unsubscribedStreams = api.getAllStreams().streams
                .filter {
                    !subscribedStreams.containsKey(it.id)
                }
            (subscribedStreams.values + unsubscribedStreams)
                .take(20)
                .map { stream ->
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
        updateCachedStreams(allStreams)
        return allStreams
    }

    override suspend fun getCachedStreams(): List<Stream> {
        val allStreams = dao.getAll()
        return allStreams.map {
            it.toStream()
        }
    }

    override suspend fun createStream(streamName: String): List<Stream> {
        val query = SubscriptionsQuery.createQuery(
            streamName = streamName
        )
        api.createStream(subscriptions = query)
        return getAllStreams()
    }

    private suspend fun updateCachedStreams(streams: List<Stream>) {
        val list = streams.map {
            it.toStreamEntity()
        }
        dao.updateStreams(list)
    }
}