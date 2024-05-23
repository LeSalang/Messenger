package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.Topic

sealed interface StreamsEffect {
    data class OpenChat(
        val topic: Topic,
        val stream: Stream
    ) : StreamsEffect
    data class ShowStreamExistsError(val streamName: String) : StreamsEffect
    data object ShowNewStreamError : StreamsEffect
}