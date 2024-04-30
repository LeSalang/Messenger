package com.lesa.app.presentation.features.streams.elm

sealed interface StreamsCommand {
    data object LoadData : StreamsCommand
}