package com.lesa.app.model_factories

import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.Topic

object StreamFactory {
    fun create(
        id: Int = 1,
        name: String = "streamName",
        isSubscribed: Boolean = true,
        color: String = "#000000",
        topics: List<Topic> = List(10) {
            TopicFactory.create(
                streamId = id,
                streamName = name,
                color = color
            )
        }
    ): Stream {
        return Stream(
            id = id,
            name = name,
            isSubscribed = isSubscribed,
            topics = topics,
            color = color
        )
    }
}