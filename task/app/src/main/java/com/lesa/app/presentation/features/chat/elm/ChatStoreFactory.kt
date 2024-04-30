package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Topic
import com.lesa.app.presentation.utils.LceState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChatStoreFactory @Inject constructor(private val actor: ChatActor) {
    fun create(topic: Topic) : Store<ChatEvent, ChatEffect, ChatState> {
        return ElmStore(
            initialState = ChatState(
                lceState = LceState.Loading,
                topic = topic,
                messages = listOf()
            ),
            reducer = ChatReducer(),
            actor = actor
        )
    }
}