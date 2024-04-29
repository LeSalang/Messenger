package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class StreamsStoreFactory @Inject constructor(private val actor: StreamsActor) {
    fun create() : Store<StreamsEvent, StreamsEffect, StreamsState> {
        return ElmStore(
            initialState = StreamsState(streamUi = ScreenState.Loading),
            reducer = StreamsReducer(),
            actor = actor
        )
    }
}