package com.lesa.app.model_factories

import com.lesa.app.domain.model.Topic

object TopicFactory {
    fun create(
        name: String = "TopicName",
        color: String = "color",
        streamName: String = "streamName",
        streamId: Int = 1
    ): Topic {
        return Topic(
            name = name,
            color = color,
            streamName = streamName,
            streamId = streamId
        )
    }
}