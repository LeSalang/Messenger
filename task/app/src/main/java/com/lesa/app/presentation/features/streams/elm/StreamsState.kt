package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.domain.model.Stream
import com.lesa.app.presentation.features.streams.model.StreamType
import com.lesa.app.presentation.features.streams.model.StreamUi
import com.lesa.app.presentation.utils.ScreenState

data class StreamsState(
    val screenState: ScreenState<List<StreamUi>>,
    val streams: List<Stream>,
    val streamType: StreamType,
    val expandedChannelId: Int? = null
)