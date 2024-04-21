package com.lesa.app.presentation.channels

import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.presentation.features.streams.model.StreamUi

class ChannelsDelegateItemFactory {
    fun makeDelegateItems(
        list: List<StreamUi>,
        expandedChannelId: Int?,
    ): MutableList<DelegateItem> {
        val result = mutableListOf<DelegateItem>()
        list.forEach { channel ->
            val isExpanded = channel.id == expandedChannelId
            result.add(
                ChannelDelegateItem(channel, isExpanded)
            )
            if (isExpanded) {
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