package com.lesa.app.domain.model

import java.util.Date

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String?,
    val presence: Presence
) {
    data class Presence(
        val status: Status,
        val timestamp: Date?
    ) : Comparable<Presence> {

        enum class Status(val order: Int) {
            ACTIVE(3),
            IDLE(2),
            OFFLINE(1)
        }

        override fun compareTo(other: Presence): Int {
            if (status == other.status) {
                return compareValues(timestamp, other.timestamp)
            }
            return this.status.order.compareTo(other.status.order)
        }
    }
}