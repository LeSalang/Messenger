package com.lesa.app.presentation.features.streams

import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.domain.model.Topic

class TopicDelegateItem  (
    val topic: Topic
) : DelegateItem {
    override val id: Any
        get() = topic.name

    override val content: Any
        get() = topic

    override fun compareToOther(other: DelegateItem): Boolean {
        return if (other is TopicDelegateItem) {
            other.topic == topic
        } else {
            false
        }
    }
}