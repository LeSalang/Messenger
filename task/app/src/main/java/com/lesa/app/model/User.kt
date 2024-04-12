package com.lesa.app.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String?,
    val netStatus: UserNetStatus
)
