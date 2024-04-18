package com.lesa.app.domain.model

data class Stream(
    val id: Int,
    val name: String,
    val isSubscribed: Boolean,
    val topics: List<Topic>,
    val color: String?
)
