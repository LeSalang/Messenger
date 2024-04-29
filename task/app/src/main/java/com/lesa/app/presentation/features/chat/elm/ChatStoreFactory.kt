package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChatStoreFactory @Inject constructor(private val actor: ChatActor) {
    fun create() : Store<ChatEvent, ChatEffect, ChatState> {
        return ElmStore(
            initialState = ChatState(
                chatUi = ScreenState.Loading,
                topicUi = null,
                messageId = null,
            ),
            reducer = ChatReducer(),
            actor = actor
        )
    }
}