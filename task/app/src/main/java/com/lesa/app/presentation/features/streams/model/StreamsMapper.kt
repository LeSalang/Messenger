package com.lesa.app.presentation.features.streams.model

import com.lesa.app.domain.model.Stream

object StreamsMapper {
    fun map(stream: Stream) : StreamUi {
        return StreamUi(
            id = stream.id,
            name = stream.name,
            isSubscribed = stream.isSubscribed,
            topics = stream.topics.sortedBy { it.name.uppercase() },
            color = stream.color
        )
    }
}