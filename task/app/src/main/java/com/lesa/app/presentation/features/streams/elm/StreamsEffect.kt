package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.model.Topic

sealed interface StreamsEffect {
    data class OpenChat(val topic: Topic) : StreamsEffect
}