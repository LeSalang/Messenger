package com.lesa.app.channels

import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.model.Topic

class TopicDelegateItem  (
    val topic: Topic
) : DelegateItem {
    override val id: Any
        get() = topic.id

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