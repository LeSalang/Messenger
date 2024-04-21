package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.use_cases.streams.LoadStreamsUseCase
import com.lesa.app.presentation.features.streams.model.StreamType
import com.lesa.app.presentation.features.streams.model.StreamsMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class StreamsActor(
    private val loadStreamsUseCase: LoadStreamsUseCase
) : Actor<StreamsCommand, StreamsEvent>() {
    override fun execute(command: StreamsCommand): Flow<StreamsEvent> {
        return when (command) {
            is StreamsCommand.LoadData -> flow {
                runCatching {
                    loadStreamsUseCase.invoke()
                }.fold(
                    onSuccess = { streamList ->
                        emit(
                            StreamsEvent.Internal.DataLoaded(
                                streamUiList = streamList
                                    .filter {
                                        when (command.streamType) {
                                            StreamType.SUBSCRIBED -> {
                                                it.isSubscribed
                                            }
                                            StreamType.ALL -> true
                                        }
                                    }
                                    .map { stream ->
                                        StreamsMapper().map(stream)
                                    }
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
}