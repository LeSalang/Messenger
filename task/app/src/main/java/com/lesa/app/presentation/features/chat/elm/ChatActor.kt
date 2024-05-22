package com.lesa.app.presentation.features.chat.elm

import com.lesa.app.domain.model.Message
import com.lesa.app.domain.model.MessageAnchor
import com.lesa.app.domain.use_cases.chat.AddReactionUseCase
import com.lesa.app.domain.use_cases.chat.DeleteMessageUseCase
import com.lesa.app.domain.use_cases.chat.DeleteReactionUseCase
import com.lesa.app.domain.use_cases.chat.EditMessageContentUseCase
import com.lesa.app.domain.use_cases.chat.LoadAllCachedMessagesUseCase
import com.lesa.app.domain.use_cases.chat.LoadMessagesInTopicUseCase
import com.lesa.app.domain.use_cases.chat.LoadSelectedMessageUseCase
import com.lesa.app.domain.use_cases.chat.SendMessageUseCase
import com.lesa.app.domain.use_cases.chat.UploadFileUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import vivid.money.elmslie.core.store.Actor
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class ChatActor @Inject constructor(
    private val addReactionUseCase: AddReactionUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val editMessageContentUseCase: EditMessageContentUseCase,
    private val loadAllCachedMessagesUseCase: LoadAllCachedMessagesUseCase,
    private val loadMessagesInTopicUseCase: LoadMessagesInTopicUseCase,
    private val loadSelectedMessageUseCase: LoadSelectedMessageUseCase,
    private val removeReactionUseCase: DeleteReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    ) : Actor<ChatCommand, ChatEvent>() {
    private val mutexMap = ConcurrentHashMap<String, Mutex>()

    override fun execute(command: ChatCommand): Flow<ChatEvent> {
        return when (command) {
            is ChatCommand.LoadAllMessages -> flow {
                val topic = command.topic
                emit(
                    loadMessagesInTopicUseCase.invoke(
                        streamName = topic.streamName,
                        topicName = topic.name,
                        anchor = MessageAnchor.Newest
                    )
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

            is ChatCommand.LoadAllCachedMessages -> flow {
                val topicName = command.topic.name
                val streamName = command.topic.streamName
                emit(loadAllCachedMessagesUseCase.invoke(
                    topicName = topicName,
                    streamName = streamName
                ))
            }.mapEvents(
                eventMapper = {
                    ChatEvent.Internal.AllCachedMessagesLoaded(
                        messages = it
                    )
                },
                errorMapper = {
                    ChatEvent.Internal.ErrorCached
                }
            )

            is ChatCommand.SendMessage -> flow {
                val messageId = sendMessageUseCase.invoke(
                    content = command.content,
                    topicName = command.topic.name,
                    streamId = command.topic.streamId
                )
                emit(loadSelectedMessageUseCase.invoke(messageId))
            }.mapEvents(
                eventMapper = { message ->
                    ChatEvent.Internal.MessageSent(
                        sentMessage = message
                    )
                },
                errorMapper = {
                   ChatEvent.Internal.ErrorMessage
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
                    ChatEvent.Internal.ErrorEmoji
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
                    ChatEvent.Internal.ErrorEmoji
                }
            )

            is ChatCommand.FetchMoreMessages -> flow {
                val topic = command.topic
                emit(
                    loadMessagesInTopicUseCase.invoke(
                        streamName = topic.streamName,
                        topicName = topic.name,
                        anchor = command.anchor
                    )
                )
            }.mapEvents(
                eventMapper = {
                    ChatEvent.Internal.OldMessagesLoaded(
                        messages = it
                    )
                },
                errorMapper = {
                    ChatEvent.Internal.Error
                }
            )

            is ChatCommand.UploadFile -> flow {
                val uri = command.uri
                val name = command.name
                emit(
                    uploadFileUseCase.invoke(
                        name = name,
                        uri = uri
                    )
                )
            }.mapEvents(
                eventMapper = {
                    ChatEvent.Internal.FileUploaded(
                        uri = it
                    )
                },
                errorMapper = {
                    ChatEvent.Internal.Error
                }
            )

            is ChatCommand.DeleteMessage -> flow {
                val messageId = command.messageId
                emit(deleteMessageUseCase.invoke(messageId = messageId))
            }.mapEvents(
                eventMapper = {
                    ChatEvent.Internal.MessageDeleted(messageId = command.messageId)
                },
                errorMapper = {
                    ChatEvent.Internal.ErrorDeleteEmoji
                }
            )

            is ChatCommand.EditMessage -> flow {
                emit(editMessageContent(command))
            }.mapEvents(
                eventMapper = {
                    ChatEvent.Internal.MessageUpdated(updatedMessage = it)
                },
                errorMapper = {
                    ChatEvent.Internal.ErrorEditMessage
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

    private suspend fun editMessageContent(command: ChatCommand.EditMessage): Message {
        editMessageContentUseCase.invoke(
            messageId = command.messageId,
            content = command.content
        )
        return loadSelectedMessageUseCase.invoke(command.messageId)
    }
}