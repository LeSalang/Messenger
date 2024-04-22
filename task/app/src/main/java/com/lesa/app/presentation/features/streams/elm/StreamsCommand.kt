package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.presentation.features.streams.model.StreamType

sealed interface StreamsCommand {
    data class LoadData(val streamType: StreamType) : StreamsCommand
    data class Search(
        val query: String,
        val streamType: StreamType
    ) : StreamsCommand
}