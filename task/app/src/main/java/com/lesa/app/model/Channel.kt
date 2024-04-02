package com.lesa.app.model

data class Channel(
    val id: Int,
    val name: String,
    val topics: List<Topic>,
    val isSubscribed: Boolean,
    val isExpanded: Boolean
)
