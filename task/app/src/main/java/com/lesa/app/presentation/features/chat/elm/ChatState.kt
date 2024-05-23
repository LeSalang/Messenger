package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Message
import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.Topic
import com.lesa.app.presentation.features.chat.models.MessageUi
import com.lesa.app.presentation.utils.LceState

data class ChatState(
    val lceState: LceState<List<MessageUi>>,
    val messages: List<Message>,
    val topic: Topic?,
    val stream: Stream,
    val isPrefetching: Boolean
)