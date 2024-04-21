package com.lesa.app.presentation.channels

import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.presentation.features.streams.model.StreamUi

class ChannelDelegateItem(
    val stream: StreamUi,
    val isExpanded: Boolean,
) : DelegateItem {
    override val id: Any
        get() = stream.id

    override val content: Any
        get() = Pair(stream, isExpanded)

    override fun compareToOther(other: DelegateItem): Boolean {
        return if (other is ChannelDelegateItem) {
            other.stream == stream && other.isExpanded == isExpanded
        } else {
            false
        }
    }
}