package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.use_cases.streams.LoadStreamsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class StreamsActor @Inject constructor(
    private val loadStreamsUseCase: LoadStreamsUseCase
) : Actor<StreamsCommand, StreamsEvent>() {

    override fun execute(command: StreamsCommand): Flow<StreamsEvent> {
        return when (command) {
            is StreamsCommand.LoadData ->  flow {
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
        }
    }
}