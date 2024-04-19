package com.lesa.app.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String?,
    val presence: Presence,
) {
    enum class Presence {
        ACTIVE,
        IDLE,
        OFFLINE
    }
}