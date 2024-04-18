package com.lesa.app.presenation.channels

import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.domain.model.Stream

class ChannelsDelegateItemFactory {
    fun makeDelegateItems(
        list: List<Stream>,
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