package com.lesa.app.presentation.features.streams.elm

import com.lesa.app.presentation.features.streams.model.StreamUi
import com.lesa.app.presentation.utils.ScreenState

data class StreamsState(
    val streamUi: ScreenState<List<StreamUi>>,
    val expandedChannelId: Int? = null
)