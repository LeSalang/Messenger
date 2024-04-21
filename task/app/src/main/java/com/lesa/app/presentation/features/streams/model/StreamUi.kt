package com.lesa.app.presentation.features.streams.model

import com.lesa.app.domain.model.Topic

data class StreamUi(
    val id: Int,
    val name: String,
    val isSubscribed: Boolean,
    val topics: List<Topic>,
    val color: String?
)