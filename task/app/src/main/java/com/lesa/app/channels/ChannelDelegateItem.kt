package com.lesa.app.channels

import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.model.Channel

class ChannelDelegateItem (
    val channel: Channel,
) : DelegateItem {
    override val id: Any
        get() = channel.id

    override val content: Any
        get() = channel

    override fun compareToOther(other: DelegateItem): Boolean {
        return if (other is ChannelDelegateItem) {
            other.channel == channel
        } else {
            false
        }
    }
}