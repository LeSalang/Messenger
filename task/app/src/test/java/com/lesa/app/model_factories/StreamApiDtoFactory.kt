package com.lesa.app.model_factories

import com.lesa.app.data.network.models.FALLBACK_STREAM_COLOR
import com.lesa.app.data.network.models.StreamApiDto

object StreamApiDtoFactory {
    fun create(
        id: Int = 1,
        name: String = "streamName",
        color: String = FALLBACK_STREAM_COLOR
    ): StreamApiDto {
        return StreamApiDto(
            id = id,
            name = name,
            color = color
        )
    }
}