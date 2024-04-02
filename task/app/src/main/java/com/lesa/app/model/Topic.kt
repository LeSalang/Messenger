package com.lesa.app.model

import androidx.annotation.ColorRes

data class Topic(
    val id: Int,
    val name: String,
    val count: Int,
    @ColorRes val color: Int
)
