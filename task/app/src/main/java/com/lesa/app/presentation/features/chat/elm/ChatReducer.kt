package com.lesa.app.presentation.features.chat.elm

import android.text.Html
import com.lesa.app.R
import com.lesa.app.domain.model.Message
import com.lesa.app.domain.model.MessageAnchor
import com.lesa.app.presentation.features.chat.elm.ChatCommand
import com.lesa.app.presentation.features.chat.elm.ChatEffect
import com.lesa.app.presentation.features.chat.elm.ChatEvent
import com.lesa.app.presentation.features.chat.message_context_menu.MessageContextMenuAction
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

            ChatEvent.Internal.ErrorDeleteEmoji -> effects {
                +Effect.MessageDeletingError
            }

            is ChatEvent.Internal.MessageDeleted -> state {
                val messages = messages.toMutableList()
                messages.removeIf { it.id == event.messageId }
                val messageUiList = messages.map { ChatMapper.map(it) }
                copy(
                    lceState = LceState.Content(messageUiList),
                    messages = messages,
                    isPrefetching = false
                )
            }

            ChatEvent.Internal.ErrorEditMessage -> effects {
                +Effect.MessageError
            }

            ChatEvent.Internal.ErrorMessageChangeTopic -> effects {
                +Effect.MessageChangeTopicError
            }

            is ChatEvent.Internal.MessageRemovedToAnotherTopic -> {
                effects {
                    +Effect.MessageMovedToAnotherTopic(topicName = event.topicName)
                }
                commands {
                    +Command.LoadAllMessages(topic = state.topic)
                }
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
                +Effect.ShowEmojiPicker(messageId = event.messageId)
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
                    uri = event.uri
                )
            }

            is ChatEvent.Ui.ShowContextMessageBottomSheet -> effects {
                val message = state.messages.firstOrNull {
                    it.id == event.messageId
                }

                +Effect.ShowMessageContextMenu(
                    messageId = event.messageId,
                    isOwn = message?.isOwn ?: false
                )
            }

            is ChatEvent.Ui.SelectMenuAction -> {
                val messageId = event.messageId
                when (event.action) {
                    MessageContextMenuAction.ADD_REACTION -> effects {
                        +Effect.ShowEmojiPicker(messageId = messageId)
                    }
                    MessageContextMenuAction.DELETE_MESSAGE -> commands {
                        +Command.DeleteMessage(messageId = messageId)
                    }
                    MessageContextMenuAction.EDIT_MESSAGE -> effects {
                        val content = state.messages.firstOrNull { it.id == messageId }?.content
                        val rawContent = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
                            .trimEnd()
                            .toString()
                        +Effect.EditMessage(
                            messageId = messageId,
                            messageContent = rawContent
                        )
                    }
                    MessageContextMenuAction.CHANGE_TOPIC -> effects {
                        +Effect.ShowChangeTopicDialog(
                            stream = state.stream,
                            messageId = event.messageId
                        )
                    }
                    MessageContextMenuAction.COPY_MESSAGE -> effects {
                        val message = state.messages.firstOrNull { it.id == messageId }
                        val text = Html.fromHtml(
                            message?.content,
                            Html.FROM_HTML_MODE_LEGACY
                        ).trimEnd().toString()
                        +Effect.MessageCopied(text = text)
                    }
                }
            }

            is ChatEvent.Ui.EditMessage -> commands {
                +Command.EditMessage(
                    messageId = event.messageId,
                    content = event.messageContent
                )
            }

            is ChatEvent.Ui.ChangeMessageTopic -> commands {
                +Command.ChangeMessageTopic(
                    messageId = event.messageId,
                    topicName = event.topicName
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