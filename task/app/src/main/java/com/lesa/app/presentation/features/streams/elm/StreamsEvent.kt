package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.model.Stream
import com.lesa.app.presentation.features.streams.model.StreamType

sealed interface StreamsEvent {
    sealed interface Ui : StreamsEvent {
        data class Init(val streamType: StreamType) : Ui
        data class ReloadStreams(val streamType: StreamType) : Ui
        data class ExpandStream(val streamId: Int?) : Ui
        data class Search(
            val query: String,
            val streamType: StreamType
        ) : Ui
    }

    sealed interface Internal : StreamsEvent {
        data class DataLoaded(val streamList: List<Stream>) : Internal
        data object Error : Internal
    }
}