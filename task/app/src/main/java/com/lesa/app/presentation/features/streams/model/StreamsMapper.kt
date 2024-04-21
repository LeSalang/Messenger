package com.lesa.app.presentation.features.streams.model

import com.lesa.app.domain.model.Stream

class StreamsMapper {
    fun map(stream: Stream) : StreamUi {
        return StreamUi(
            id = stream.id,
            name = stream.name,
            isSubscribed = stream.isSubscribed,
            topics = stream.topics,
            color = stream.color
        )
    }
}