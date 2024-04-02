package com.lesa.app.channels

import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.model.Channel

class ChannelsDelegateItemFactory {
    fun makeDelegateItems(
        list: List<Channel>,
    ): MutableList<DelegateItem> {
        val result = mutableListOf<DelegateItem>()
        list.forEach {channel ->
            result.add(
                ChannelDelegateItem(channel)
            )
            if (channel.isExpanded) {
                channel.topics.forEach { topic ->
                    result.add(
                        TopicDelegateItem(topic)
                    )
                }
            }
        }
        return result
    }
}