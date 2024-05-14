package com.lesa.app.model_factories

import com.lesa.app.data.network.models.PresenceApiDto

object PresenceApiDtoFactory {
    fun create(
        status: String = "active",
        timestamp: Long = 1000000000
    ): PresenceApiDto {
        return PresenceApiDto(
            aggregated = PresenceApiDto.Aggregated(
                status = status,
                timestamp = timestamp
            )
        )
    }
}