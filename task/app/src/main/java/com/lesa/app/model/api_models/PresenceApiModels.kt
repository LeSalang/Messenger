package com.lesa.app.model.api_models

import com.lesa.app.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllPresenceApiDto(
    @SerialName("presences")
    val presences: Map<String, PresenceDto>
)

@Serializable
data class PresenceApiDto(
    @SerialName("result") val result: String,
    @SerialName("msg") val msg: String,
    @SerialName("presence") val presence: PresenceDto
)

@Serializable
data class PresenceDto(
    @SerialName("aggregated") val aggregated: PresenceDataDto,
)

@Serializable
data class PresenceDataDto(
    @SerialName("status") val status: String,
    @SerialName("timestamp") val timestamp: Long,
)

fun PresenceDto.toPresence() : User.Presence {
    return when (aggregated.status) {
        PresenceTypeDto.ACTIVE.value -> User.Presence.ACTIVE
        PresenceTypeDto.IDLE.value -> User.Presence.IDLE
        PresenceTypeDto.OFFLINE.value -> User.Presence.OFFLINE
        else -> throw RuntimeException("Unknown presence status ${aggregated.status}")
    }
}

enum class PresenceTypeDto(val value: String) {
    ACTIVE("active"),
    IDLE("idle"),
    OFFLINE("offline")
}