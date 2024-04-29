package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Message
import com.lesa.app.presentation.features.chat.elm.ChatCommand
import com.lesa.app.presentation.features.chat.elm.ChatEffect
import com.lesa.app.presentation.features.chat.elm.ChatEvent
import com.lesa.app.presentation.features.chat.models.ChatMapper
import com.lesa.app.presentation.features.chat.models.emojiSetCNCS
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
                val messageUiList = event.messages.map {
                    ChatMapper().map(it)
                }
                copy(
                    screenState = ScreenState.Content(messageUiList),
                    messages = event.messages
                )
            }

            Event.Internal.Error -> state {
                copy(
                    screenState = ScreenState.Error
                )
            }

            ChatEvent.Internal.ErrorMessage -> effects {
                +ChatEffect.EmojiError
            }

            is ChatEvent.Internal.MessageSent -> state {
                val messages = state.messages + event.sentMessage
                val messageUiList = messages.map {
                    ChatMapper().map(it)
                }
                copy(
                    screenState = ScreenState.Content(messageUiList),
                    messages = messages
                )
            }

            is ChatEvent.Internal.MessageUpdated -> state {
                val messageIndex = messages.indexOfFirst {
                    it.id == event.updatedMessage.id
                }
                val messageList = messages.toMutableList()
                messageList[messageIndex] = event.updatedMessage
                val messageUiList = messageList.map {
                    ChatMapper().map(it)
                }
                copy(
                    screenState = ScreenState.Content(messageUiList),
                    messages = messageList
                )
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
                val command = selectEmoji(
                    messageList = state.messages,
                    messageId = event.messageId,
                    emojiCode = event.emojiCode
                )
                if (command != null) +command
            }
        }
    }

    private fun selectEmoji(
        messageList: List<Message>,
        messageId: Int,
        emojiCode: String
    ) : ChatCommand? {
        val message = messageList.firstOrNull {
            it.id == messageId
        } ?: return null
        val emoji = message.reactions[emojiCode]
        if (emoji != null) {
            val emojiName = emoji.emojiName
            return if (emoji.isOwn) {
                ChatCommand.RemoveReaction(
                    messageId = message.id, emojiName = emojiName
                )
            } else {
                ChatCommand.AddReaction(
                    messageId = message.id, emojiName = emojiName
                )
            }
        } else {
            val emojiName = emojiSetCNCS.firstOrNull {
                it.code == emojiCode
            }?.name ?: return null
            return ChatCommand.AddReaction(
                messageId = message.id,
                emojiName = emojiName
            )
        }
    }
}