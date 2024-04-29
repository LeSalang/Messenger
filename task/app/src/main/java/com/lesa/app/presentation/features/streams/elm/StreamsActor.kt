package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.use_cases.streams.LoadStreamsUseCase
import com.lesa.app.presentation.features.streams.model.StreamType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class StreamsActor @Inject constructor(
    private val loadStreamsUseCase: LoadStreamsUseCase
) : Actor<StreamsCommand, StreamsEvent>() {
    private var streams = listOf<Stream>()

    override fun execute(command: StreamsCommand): Flow<StreamsEvent> {
        return when (command) {
            is StreamsCommand.LoadData ->  flow {
                runCatching {
                    loadStreamsUseCase.invoke()
                }.fold(
                    onSuccess = { streamList ->
                        streams = streamList
                        val filteredStreams = streams.filter {
                            when (command.streamType) {
                                StreamType.SUBSCRIBED -> {
                                    it.isSubscribed
                                }
                                StreamType.ALL -> true
                            }
                        }
                        emit(
                            StreamsEvent.Internal.DataLoaded(
                                streamList = filteredStreams
                            )
                        )
                    },
                    onFailure = {
                        emit(StreamsEvent.Internal.Error)
                    }
                )
            }
            is StreamsCommand.Search -> flow {
                runCatching {
                    filter(
                        streamList = search(command.query),
                        streamType = command.streamType
                    )
                }.fold(
                    onSuccess = {
                        emit(
                            StreamsEvent.Internal.DataLoaded(
                                streamList = it
                            )
                        )
                    },
                    onFailure = {
                        emit(StreamsEvent.Internal.Error)
                    }
                )
            }
        }
    }

    private fun filter(streamList: List<Stream>, streamType: StreamType) : List<Stream> {
        return streamList.filter {
            when (streamType) {
                StreamType.SUBSCRIBED -> {
                    it.isSubscribed
                }
                StreamType.ALL -> true
            }
        }
    }

    private fun search(query: String): List<Stream> {
        val refactoredQuery = query.trim(' ')
        if (refactoredQuery.isEmpty()) return streams
        val list = streams.filter { stream ->
            stream.topics.any { topic ->
                topic.name.contains(refactoredQuery, ignoreCase = true)
            } || stream.name.contains(refactoredQuery, ignoreCase = true)
        }
        return list
    }
}