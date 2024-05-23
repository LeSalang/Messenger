package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.model.Stream
import com.lesa.app.presentation.features.streams.elm.StreamsEvent
import com.lesa.app.presentation.features.streams.model.StreamType
import com.lesa.app.presentation.features.streams.model.StreamsMapper
import com.lesa.app.presentation.utils.LceState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import com.lesa.app.presentation.features.streams.elm.StreamsCommand as Command
import com.lesa.app.presentation.features.streams.elm.StreamsEffect as Effect
import com.lesa.app.presentation.features.streams.elm.StreamsEvent as Event
import com.lesa.app.presentation.features.streams.elm.StreamsState as State

class StreamsReducer : ScreenDslReducer<Event, Event.Ui, Event.Internal, State, Effect, Command> (
    Event.Ui::class,
    Event.Internal::class
) {
    override fun Result.internal(event: Event.Internal): Any {
        return when (event) {
            is Event.Internal.DataLoaded -> state {
                val filteredStreams = filter(
                    streamType = state.streamType,
                    streamList = event.streams
                )
                val streamUiList = filteredStreams
                    .sortedBy { it.name.uppercase() }
                    .map { StreamsMapper.map(it) }
                copy(
                    lceState = LceState.Content(streamUiList),
                    streams = event.streams
                )
            }
            Event.Internal.Error -> state {
                if (streams.isEmpty()) {
                    copy(
                        lceState = LceState.Error
                    )
                } else {
                    return@state this
                }
            }

            is StreamsEvent.Internal.CachedDataLoaded -> {
                state {
                    val filteredStreams = filter(
                        streamType = state.streamType,
                        streamList = event.streams
                    )
                    val streams = filteredStreams
                        .sortedBy { it.name.uppercase() }
                        .map { StreamsMapper.map(it) }
                    if (streams.isEmpty()) {
                        copy(
                            lceState = LceState.Loading
                        )
                    } else {
                        copy(
                            lceState = LceState.Content(streams),
                            streams = event.streams
                        )
                    }
                }
                commands {
                    +Command.LoadStreams
                }
            }
            StreamsEvent.Internal.ErrorCached -> {
                state {
                    copy(
                        lceState = LceState.Loading
                    )
                }
                commands {
                    +Command.LoadStreams
                }
            }

            StreamsEvent.Internal.CreateStreamError -> effects {
                +Effect.ShowNewStreamError
            }
        }
    }

    override fun Result.ui(event: Event.Ui): Any {
        return when (event) {
            is Event.Ui.Init -> commands {
                +Command.LoadCachedStreams
            }
            is Event.Ui.ReloadStreams -> commands {
                +Command.LoadStreams
            }
            is StreamsEvent.Ui.ExpandStream -> state {
                copy(
                    expandedChannelId = if (event.streamId == this.expandedChannelId) {
                        null
                    } else {
                        event.streamId
                    }
                )
            }
            is StreamsEvent.Ui.Search -> state {
                val filteredStreams = filter(
                    streamType = state.streamType,
                    streamList = state.streams
                )
                val resultList = search(streams = filteredStreams, query = event.query)
                    .map { StreamsMapper.map(it) }
                copy(
                    lceState = LceState.Content(resultList)
                )
            }

            is StreamsEvent.Ui.TopicClicked -> effects {
                +Effect.OpenTopic(
                    topic = event.topic,
                    stream = event.stream
                )
            }

            is StreamsEvent.Ui.StreamClicked -> effects {
                +Effect.OpenStream(
                    stream = event.stream
                )
            }

            is StreamsEvent.Ui.CreateStream -> {
                val existingStream = state.streams.firstOrNull() {
                    it.name == event.streamName
                }
                if (existingStream == null) {
                    commands {
                        +Command.CreateStream(event.streamName)
                    }
                } else {
                    effects {
                        +Effect.ShowStreamExistsError(streamName =  event.streamName)
                    }
                }
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

    private fun search(streams: List<Stream>, query: String): List<Stream> {
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