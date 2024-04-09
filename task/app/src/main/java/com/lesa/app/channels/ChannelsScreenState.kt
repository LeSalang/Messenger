package com.lesa.app.channels

import com.lesa.app.model.Channel

sealed interface ChannelsScreenState {

    object Initial : ChannelsScreenState

    object Loading : ChannelsScreenState

    object Error : ChannelsScreenState

    class DataLoaded(val list: List<Channel>, val expandedChannelId: Int? = null) : ChannelsScreenState
}