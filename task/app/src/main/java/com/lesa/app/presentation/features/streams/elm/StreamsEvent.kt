package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.presentation.features.streams.model.StreamType
import com.lesa.app.presentation.features.streams.model.StreamUi

sealed interface StreamsEvent {
    sealed interface Ui : StreamsEvent {
        data class Init(val streamType: StreamType) : Ui
        data class ReloadStreams(val streamType: StreamType) : Ui
        data class ExpandStream(val streamId: Int?) : Ui
    }

    sealed interface Internal : StreamsEvent {
        data class DataLoaded(val streamUiList: List<StreamUi>) : Internal
        data object Error : Internal
    }
}