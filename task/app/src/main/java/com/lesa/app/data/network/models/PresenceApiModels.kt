package com.lesa.app.data.network.models

import com.lesa.app.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class AllPresenceApiDto(
    @SerialName("presences")
    val presences: Map<String, PresenceApiDto>
)

@Serializable
data class PresenceResponseApiDto(
    @SerialName("result") val result: String,
    @SerialName("msg") val msg: String,
    @SerialName("presence") val presence: PresenceApiDto
)

@Serializable
data class PresenceApiDto(
    @SerialName("aggregated") val aggregated: Aggregated,
) {
    @Serializable
    data class Aggregated(
        @SerialName("status") val status: String,
        @SerialName("timestamp") val timestamp: Long,
    )
}

fun PresenceApiDto?.toPresence() : User.Presence {
    if (this == null) return User.Presence(
        status = User.Presence.Status.OFFLINE,
        timestamp = null
    )
    val status = when (aggregated.status) {
        PresenceTypeDto.ACTIVE.value -> User.Presence.Status.ACTIVE
        PresenceTypeDto.IDLE.value -> User.Presence.Status.IDLE
        PresenceTypeDto.OFFLINE.value -> User.Presence.Status.OFFLINE
        else -> throw RuntimeException("Unknown presence status ${aggregated.status}")
    }
    val timestamp = Date(aggregated.timestamp * 1000)
    val presence = User.Presence(
        status = status,
        timestamp = timestamp
    )
    return presence
}

enum class PresenceTypeDto(val value: String) {
    ACTIVE("active"),
    IDLE("idle"),
    OFFLINE("offline")
}