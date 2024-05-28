package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.Topic

sealed interface StreamsEvent {
    sealed interface Ui : StreamsEvent {
        data class CreateStream(val streamName: String) : Ui
        data class ExpandStream(val streamId: Int?) : Ui
        data class Search(val query: String) : Ui
        data class StreamClicked(val stream: Stream) : Ui
        data class TopicClicked(val topic: Topic, val stream: Stream) : Ui
        data object Init : Ui
        data object ReloadStreams : Ui
    }

    sealed interface Internal : StreamsEvent {
        data class CachedDataLoaded(val streams: List<Stream>) : Internal
        data class DataLoaded(val streams: List<Stream>) : Internal
        data object CreateStreamError : Internal
        data object Error : Internal
        data object ErrorCached : Internal
    }
}