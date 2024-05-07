package com.lesa.app.domain.model

sealed class MessageAnchor {
    data object Newest : MessageAnchor()
    data class Message(val id: Int) : MessageAnchor()
}