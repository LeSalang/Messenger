package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Message
import com.lesa.app.domain.use_cases.chat.AddReactionUseCase
import com.lesa.app.domain.use_cases.chat.DeleteReactionUseCase
import com.lesa.app.domain.use_cases.chat.LoadAllMessagesUseCase
import com.lesa.app.domain.use_cases.chat.LoadSelectedMessageUseCase
import com.lesa.app.domain.use_cases.chat.SendMessageUseCase
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
    private val removeReactionUseCase: DeleteReactionUseCase
    ) : Actor<ChatCommand, ChatEvent>() {
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
                    ChatEvent.Internal.AllMessagesLoaded(
                        messages = it
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
                    ChatEvent.Internal.MessageSent(
                        sentMessage = message
                    )
                },
                errorMapper = {
                   ChatEvent.Internal.Error
                }
            )

            is ChatCommand.AddReaction -> flow {
                mutexMap.getOrPut(command.messageId.toString(), ::Mutex).withLock {
                    emit(addReaction(command))
                }
            }.mapEvents(
                eventMapper = {
                    ChatEvent.Internal.MessageUpdated(
                        updatedMessage = it
                    )
                },
                errorMapper = {
                    ChatEvent.Internal.ErrorMessage
                }
            )

            is ChatCommand.RemoveReaction -> flow {
                mutexMap.getOrPut(command.messageId.toString(), ::Mutex).withLock {
                    emit(removeReaction(command))
                }
            }.mapEvents(
                eventMapper = {
                    ChatEvent.Internal.MessageUpdated(
                        updatedMessage = it
                    )
                },
                errorMapper = {
                    ChatEvent.Internal.ErrorMessage
                }
            )
        }
    }

    private suspend fun addReaction(command: ChatCommand.AddReaction): Message {
        addReactionUseCase.invoke(
            messageId = command.messageId,
            emojiName = command.emojiName
        )
        return loadSelectedMessageUseCase.invoke(command.messageId)
    }

    private suspend fun removeReaction(command: ChatCommand.RemoveReaction): Message {
        removeReactionUseCase.invoke(
            messageId = command.messageId,
            emojiName = command.emojiName
        )
        return loadSelectedMessageUseCase.invoke(command.messageId)
    }
}