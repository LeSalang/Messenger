package com.lesa.app.presentation.features.streams

import com.lesa.app.presentation.composite_adapter.DelegateItem
import com.lesa.app.presentation.features.streams.model.StreamUi

class StreamDelegateItemFactory {
    fun makeDelegateItems(
        list: List<StreamUi>,
        expandedStreamId: Int?,
    ): MutableList<DelegateItem> {
        val result = mutableListOf<DelegateItem>()
        list.forEach { streamUi ->
            val isExpanded = streamUi.id == expandedStreamId
            result.add(
                StreamDelegateItem(streamUi, isExpanded)
            )
            if (isExpanded) {
                streamUi.topics.forEach { topic ->
                    result.add(
                        TopicDelegateItem(topic)
                    )
                }
            }
        }
        return result
    }
}