package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.R
import com.lesa.app.domain.model.Message
import com.lesa.app.domain.model.MessageAnchor
import com.lesa.app.presentation.features.chat.elm.ChatCommand
import com.lesa.app.presentation.features.chat.elm.ChatEffect
import com.lesa.app.presentation.features.chat.elm.ChatEvent
import com.lesa.app.presentation.features.chat.models.ChatMapper
import com.lesa.app.presentation.features.chat.models.emojiSetCNCS
import com.lesa.app.presentation.utils.LceState
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
                    ChatMapper.map(it)
                }
                copy(
                    lceState = LceState.Content(messageUiList),
                    messages = event.messages
                )
            }

            is Event.Internal.AllCachedMessagesLoaded -> {
                state {
                    val messageUiList = event.messages.map {
                        ChatMapper.map(it)
                    }
                    if (event.messages.isEmpty()) {
                        copy(
                            lceState = LceState.Loading
                        )
                    } else {
                        copy(
                            lceState = LceState.Content(messageUiList),
                            messages = event.messages
                        )
                    }
                }
                commands {
                    +Command.LoadAllMessages(topic = state.topic)
                }
            }

            Event.Internal.Error -> state {
                if (messages.isEmpty()) {
                    copy(
                        lceState = LceState.Error,
                        isPrefetching = false
                    )
                } else {
                    return@state this
                }
            }

            Event.Internal.ErrorCached -> {
                state {
                    copy(
                        lceState = LceState.Loading
                    )
                }
                commands {
                    +Command.LoadAllMessages(topic = state.topic)
                }
            }

            ChatEvent.Internal.ErrorEmoji -> effects {
                +ChatEffect.EmojiError
            }

            ChatEvent.Internal.ErrorMessage -> effects {
                +ChatEffect.MessageError
            }

            is ChatEvent.Internal.MessageSent -> state {
                val messages = state.messages + event.sentMessage
                val messageUiList = messages.map {
                    ChatMapper.map(it)
                }
                copy(
                    lceState = LceState.Content(messageUiList),
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
                    ChatMapper.map(it)
                }
                copy(
                    lceState = LceState.Content(messageUiList),
                    messages = messageList
                )
            }

            is ChatEvent.Internal.OldMessagesLoaded -> state {
                val messages = event.messages + state.messages
                val messageUiList = messages.map {
                    ChatMapper.map(it)
                }
                copy(
                    lceState = LceState.Content(messageUiList),
                    messages = messages,
                    isPrefetching = false
                )
            }

            is ChatEvent.Internal.FileUploaded -> commands {
                val content = "[](${event.uri})"
                +Command.SendMessage(topic = state.topic, content = content)
            }
        }
    }

    override fun Result.ui(event: Event.Ui): Any {
        return when (event) {
            is Event.Ui.Init -> commands {
                val topic = state.topic
                +Command.LoadAllCachedMessages(topic = topic)
            }

            is Event.Ui.SelectEmoji -> commands {
                val command = selectEmoji(
                    messageList = state.messages,
                    messageId = event.messageId,
                    emojiCode = event.emojiCode
                )
                if (command != null) +command
            }

            is Event.Ui.ShowEmojiPicker -> effects {
                +Effect.ShowEmojiPicker(emojiId = event.emojiId)
            }

            is Event.Ui.ActionButtonClicked -> {
                if (event.content.isBlank()) {
                    effects {
                        +Effect.ShowAttachmentsPicker
                    }
                } else {
                    commands {
                        +Command.SendMessage(content = event.content, topic = state.topic)
                    }
                    effects {
                        +Effect.ClearMessageInput
                    }
                }
            }

            Event.Ui.Back -> effects {
                +Effect.Back
            }

            Event.Ui.ReloadChat -> commands {
                +Command.LoadAllMessages(topic = state.topic)
            }

            is ChatEvent.Ui.MessageTextChanged -> effects {
                if (event.text.isBlank()) {
                    +Effect.UpdateActionButton(
                        icon = R.drawable.circle_button_add_message_icon,
                        background = R.drawable.circle_button_add_file_bg
                    )
                } else {
                    +Effect.UpdateActionButton(
                        icon = R.drawable.circle_button_add_file_icon,
                        background = R.drawable.circle_button_add_message_bg
                    )
                }
            }

            ChatEvent.Ui.FetchMoreMessages ->
                if (!state.isPrefetching){
                    state {
                        copy(
                            isPrefetching = true
                        )
                    }
                    commands {
                        val oldestMessageId = state.messages.firstOrNull()?.id
                        if (oldestMessageId != null) {
                            val anchor = MessageAnchor.Message(id = oldestMessageId)
                            +Command.FetchMoreMessages(topic = state.topic, anchor = anchor)
                        }
                    }
                } else {
                    Unit
                }

            is ChatEvent.Ui.UploadFile -> commands {
                +Command.UploadFile(
                    name = event.name,
                    uri = event.uri,
                    contentResolver = event.contentResolver
                )
            }
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