package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.presentation.features.chat.models.MessageUi
import com.lesa.app.presentation.features.chat.models.TopicUi
import com.lesa.app.presentation.utils.ScreenState

data class ChatState(
    val chatUi: ScreenState<List<MessageUi>>,
    val topicUi: TopicUi?,
    val messageId: Int?,
)