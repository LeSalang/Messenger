package com.lesa.app.presenation.channels

import com.lesa.app.domain.model.Stream

sealed interface ChannelsScreenState {

    object Initial : ChannelsScreenState

    object Loading : ChannelsScreenState

    object Error : ChannelsScreenState

    class DataLoaded(
        val list: List<Stream>,
        val expandedChannelId: Int? = null
    ) : ChannelsScreenState
}