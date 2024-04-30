package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.presentation.features.streams.model.StreamType
import com.lesa.app.presentation.utils.LceState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class StreamsStoreFactory @Inject constructor(private val actor: StreamsActor) {
    fun create(streamType: StreamType) : Store<StreamsEvent, StreamsEffect, StreamsState> {
        return ElmStore(
            initialState = StreamsState(
                lceState = LceState.Loading,
                streams = listOf(),
                streamType = streamType
            ),
            reducer = StreamsReducer(),
            actor = actor
        )
    }
}