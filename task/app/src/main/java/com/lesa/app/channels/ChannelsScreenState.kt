package com.lesa.app.channels

import com.lesa.app.model.Stream

sealed interface ChannelsScreenState {

    object Initial : ChannelsScreenState

    object Loading : ChannelsScreenState

    object Error : ChannelsScreenState

    class DataLoaded(
        val list: List<Stream>,
        val expandedChannelId: Int? = null
    ) : ChannelsScreenState
}