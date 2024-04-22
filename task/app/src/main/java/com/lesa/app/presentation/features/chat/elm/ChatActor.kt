package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Message
import com.lesa.app.domain.use_cases.chat.AddReactionUseCase
import com.lesa.app.domain.use_cases.chat.DeleteReactionUseCase
import com.lesa.app.domain.use_cases.chat.LoadAllMessagesUseCase
import com.lesa.app.domain.use_cases.chat.LoadSelectedMessageUseCase
import com.lesa.app.domain.use_cases.chat.SendMessageUseCase
import com.lesa.app.presentation.features.chat.models.emojiSetCNCS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class ChatActor(
    private val loadAllMessagesUseCase: LoadAllMessagesUseCase, 
    private val sendMessageUseCase: SendMessageUseCase,
    private val loadSelectedMessageUseCase: LoadSelectedMessageUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val deleteReactionUseCase: DeleteReactionUseCase
    ) : Actor<ChatCommand, ChatEvent>() {
    private var messageList = mutableListOf<Message>()

    override fun execute(command: ChatCommand): Flow<ChatEvent> {
        return when (command) {
            is ChatCommand.LoadAllMessages -> flow {
                runCatching {
                    val topic = command.topicUi
                    loadAllMessagesUseCase.invoke(streamName = topic.streamName, topicName = topic.name)
                }.fold(
                    onSuccess = { list ->
                        messageList = list.toMutableList()
                        emit(
                            ChatEvent.Internal.AllMessagesLoaded(
                                messageList = messageList
                            )
                        )
                    },
                    onFailure = {
                        emit(ChatEvent.Internal.Error)
                    }
                )
            }
            is ChatCommand.SendMessage -> flow {
                runCatching { 
                    val messageId = sendMessageUseCase.invoke(
                        content = command.content,
                        topicName = command.topicUi.name,
                        streamId = command.topicUi.streamId
                    )
                    loadSelectedMessageUseCase.invoke(messageId)
                }.fold(
                    onSuccess = { message ->
                        messageList.add(message)
                        emit(
                            ChatEvent.Internal.AllMessagesLoaded(
                                messageList = messageList
                            )
                        )
                    },
                    onFailure = {
                        emit(ChatEvent.Internal.Error)
                    }
                )
            }
            is ChatCommand.SelectEmoji -> flow {
                runCatching {
                    val message = messageList.firstOrNull {
                        it.id == command.messageId
                    } ?: return@flow
                    val emoji = message.reactions[command.emojiCode]
                    if (emoji != null) {
                        val emojiName = emoji.emojiName
                        if (emoji.isOwn) {
                            deleteReactionUseCase.invoke(
                                messageId = message.id,
                                emojiName = emojiName
                            )
                        } else {
                            addReactionUseCase.invoke(
                                messageId = message.id,
                                emojiName = emojiName
                            )
                        }
                    } else {
                        val emojiName = emojiSetCNCS.firstOrNull {
                            it.code == command.emojiCode
                        }?.name ?: return@flow
                        addReactionUseCase.invoke(
                            messageId = message.id,
                            emojiName = emojiName
                        )
                    }
                    return@runCatching loadSelectedMessageUseCase.invoke(command.messageId)
                }.fold(
                    onSuccess = { message ->
                        val index = messageList.indexOfFirst {
                            it.id == message.id
                        }
                        messageList[index] = message
                        emit(
                            ChatEvent.Internal.AllMessagesLoaded(
                                messageList = messageList
                            )
                        )
                    },
                    onFailure = {
                        emit(ChatEvent.Internal.Error)
                    }
                )
            }
        }
    }
}