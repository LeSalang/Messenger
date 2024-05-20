package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.use_cases.streams.CreateStreamUseCase
import com.lesa.app.domain.use_cases.streams.LoadCachedStreamsUseCase
import com.lesa.app.domain.use_cases.streams.LoadStreamsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class StreamsActor @Inject constructor(
    private val loadStreamsUseCase: LoadStreamsUseCase,
    private val loadCachedStreamsUseCase: LoadCachedStreamsUseCase,
    private val createStreamUseCase: CreateStreamUseCase
) : Actor<StreamsCommand, StreamsEvent>() {

    override fun execute(command: StreamsCommand): Flow<StreamsEvent> {
        return when (command) {
            is StreamsCommand.LoadStreams ->  flow {
                emit(loadStreamsUseCase.invoke())
            }.mapEvents(
                eventMapper = { streams ->
                    StreamsEvent.Internal.DataLoaded(
                        streams = streams
                    )
                },
                errorMapper = {
                    StreamsEvent.Internal.Error
                }
            )

            StreamsCommand.LoadCachedStreams -> flow {
                emit(loadCachedStreamsUseCase.invoke())
            }.mapEvents(
                eventMapper = { streams ->
                    StreamsEvent.Internal.CachedDataLoaded(
                        streams = streams
                    )
                },
                errorMapper = {
                    StreamsEvent.Internal.ErrorCached
                }
            )

            is StreamsCommand.CreateStream -> flow {
                emit(createStreamUseCase.invoke(streamName = command.streamName))
            }.mapEvents(
                eventMapper = { streams ->
                    StreamsEvent.Internal.DataLoaded(
                        streams = streams
                    )
                },
                errorMapper = {
                    StreamsEvent.Internal.CreateStreamError
                }
            )
        }
    }
}