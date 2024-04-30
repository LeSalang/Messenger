package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Message
import com.lesa.app.presentation.features.chat.models.MessageUi
import com.lesa.app.presentation.features.chat.models.TopicUi
import com.lesa.app.presentation.utils.ScreenState

data class ChatState(
    val screenState: ScreenState<List<MessageUi>>,
    val messages: List<Message>,
    val topicUi: TopicUi
)