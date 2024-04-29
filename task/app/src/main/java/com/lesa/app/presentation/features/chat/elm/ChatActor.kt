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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import vivid.money.elmslie.core.store.Actor
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class ChatActor @Inject constructor(
    private val loadAllMessagesUseCase: LoadAllMessagesUseCase, 
    private val sendMessageUseCase: SendMessageUseCase,
    private val loadSelectedMessageUseCase: LoadSelectedMessageUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val deleteReactionUseCase: DeleteReactionUseCase
    ) : Actor<ChatCommand, ChatEvent>() {
    private var messageList = mutableListOf<Message>()
    private val mutexMap = ConcurrentHashMap<String, Mutex>()

    override fun execute(command: ChatCommand): Flow<ChatEvent> {
        return when (command) {
            is ChatCommand.LoadAllMessages -> flow {
                val topic = command.topicUi
                emit(
                    loadAllMessagesUseCase.invoke(streamName = topic.streamName, topicName = topic.name)
                )
            }.mapEvents(
                eventMapper = {
                    messageList = it.toMutableList()
                    ChatEvent.Internal.AllMessagesLoaded(
                        messageList = messageList
                    )
                },
                errorMapper = {
                    ChatEvent.Internal.Error
                }
            )
            is ChatCommand.SendMessage -> flow {
                val messageId = sendMessageUseCase.invoke(
                    content = command.content,
                    topicName = command.topicUi.name,
                    streamId = command.topicUi.streamId
                )
                emit(loadSelectedMessageUseCase.invoke(messageId))
                }.mapEvents(
                    eventMapper = { message ->
                        messageList.add(message)
                            ChatEvent.Internal.AllMessagesLoaded(
                                messageList = messageList
                            )
                    },
                    errorMapper = {
                       ChatEvent.Internal.Error
                    }
                )
            is ChatCommand.SelectEmoji -> flow {
                mutexMap.getOrPut(command.messageId.toString(), ::Mutex).withLock {
                    emit(
                        selectEmoji(
                            messageId = command.messageId,
                            emojiCode = command.emojiCode
                        )
                    )
                }
            }.mapEvents(
                eventMapper = {
                    ChatEvent.Internal.AllMessagesLoaded(
                        messageList = messageList
                    )
                },
                errorMapper = {
                    ChatEvent.Internal.ErrorMessage
                }
            )
        }
    }

    private suspend fun selectEmoji(messageId: Int, emojiCode: String) {
            val message = messageList.firstOrNull {
                it.id == messageId
            } ?: return
            val emoji = message.reactions[emojiCode]
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
                    it.code == emojiCode
                }?.name ?: return
                addReactionUseCase.invoke(
                    messageId = message.id,
                    emojiName = emojiName
                )
            }
            val updatedMessage = loadSelectedMessageUseCase.invoke(messageId)
            val index = messageList.indexOfFirst {
                it.id == updatedMessage.id
            }
            messageList[index] = updatedMessage
    }
}