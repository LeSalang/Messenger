package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.presentation.features.chat.elm.ChatEffect
import com.lesa.app.presentation.features.chat.elm.ChatEvent
import com.lesa.app.presentation.features.chat.models.ChatMapper
import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import com.lesa.app.presentation.features.chat.elm.ChatCommand as Command
import com.lesa.app.presentation.features.chat.elm.ChatEffect as Effect
import com.lesa.app.presentation.features.chat.elm.ChatEvent as Event
import com.lesa.app.presentation.features.chat.elm.ChatState as State

class ChatReducer : ScreenDslReducer<Event, Event.Ui, Event.Internal, State, Effect, Command> (
    Event.Ui::class,
    Event.Internal::class
) {
    override fun Result.internal(event: Event.Internal): Any {
        return when (event) {
            is Event.Internal.AllMessagesLoaded -> state {
                val messageUiList = event.messageList.map {
                    ChatMapper().map(it)
                }
                copy(
                    chatUi = ScreenState.Content(messageUiList)
                )
            }
            Event.Internal.Error -> state {
                copy(
                    chatUi = ScreenState.Error
                )
            }
            ChatEvent.Internal.ErrorMessage -> effects {
                +ChatEffect.EmojiError
            }
        }
    }

    override fun Result.ui(event: Event.Ui): Any {
        return when (event) {
            is Event.Ui.Init -> commands {
                val topic = event.topicUi
                +Command.LoadAllMessages(topicUi = topic)
            }
            is Event.Ui.SendMessage -> commands {
                +Command.SendMessage(content = event.content, topicUi = event.topicUi)
            }
            Event.Ui.ReloadChat -> commands {
                +Command.LoadAllMessages(topicUi = state.topicUi!!) //TODO
            }
            is Event.Ui.SelectEmoji -> commands {
                +Command.SelectEmoji(
                    messageId = event.messageId,
                    emojiCode = event.emojiCode
                )
            }
        }
    }
}