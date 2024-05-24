package com.lesa.app.presentation.features.chat

import com.lesa.app.domain.model.MessageAnchor
import com.lesa.app.domain.use_cases.chat.AddReactionUseCase
import com.lesa.app.domain.use_cases.chat.ChangeMessageTopicUseCase
import com.lesa.app.domain.use_cases.chat.DeleteMessageUseCase
import com.lesa.app.domain.use_cases.chat.DeleteReactionUseCase
import com.lesa.app.domain.use_cases.chat.EditMessageContentUseCase
import com.lesa.app.domain.use_cases.chat.LoadAllCachedMessagesUseCase
import com.lesa.app.domain.use_cases.chat.LoadMessagesInTopicUseCase
import com.lesa.app.domain.use_cases.chat.LoadSelectedMessageUseCase
import com.lesa.app.domain.use_cases.chat.SendMessageUseCase
import com.lesa.app.domain.use_cases.chat.UploadFileUseCase
import com.lesa.app.model_factories.MessageFactory
import com.lesa.app.model_factories.TopicFactory
import com.lesa.app.presentation.features.chat.elm.ChatActor
import com.lesa.app.presentation.features.chat.elm.ChatCommand
import com.lesa.app.presentation.features.chat.elm.ChatEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ChatActorTest : BehaviorSpec({

    Given("chat actor") {
        val addReactionUseCase: AddReactionUseCase = mockk()
        val changeMessageTopicUseCase: ChangeMessageTopicUseCase = mockk()
        val deleteMessageUseCase: DeleteMessageUseCase = mockk()
        val editMessageContentUseCase: EditMessageContentUseCase = mockk()
        val loadAllCachedMessagesUseCase: LoadAllCachedMessagesUseCase = mockk()
        val loadMessagesInTopicUseCase: LoadMessagesInTopicUseCase = mockk()
        val loadSelectedMessageUseCase: LoadSelectedMessageUseCase = mockk()
        val removeReactionUseCase: DeleteReactionUseCase = mockk()
        val sendMessageUseCase: SendMessageUseCase = mockk()
        val uploadFileUseCase: UploadFileUseCase = mockk()
        val chatActor = ChatActor(
            addReactionUseCase,
            changeMessageTopicUseCase,
            deleteMessageUseCase,
            editMessageContentUseCase,
            loadAllCachedMessagesUseCase,
            loadMessagesInTopicUseCase,
            loadSelectedMessageUseCase,
            removeReactionUseCase,
            sendMessageUseCase,
            uploadFileUseCase
        )
        val message = MessageFactory.create()
        val messages = List(10) { message }
        val topic = TopicFactory.create(name = "topic")
        val topicName = "topic"
        val streamName = "stream"

        When("command is LoadAllMessages") {
            And("loading is successful") {
                coEvery {
                    loadMessagesInTopicUseCase.invoke(
                        streamName = any(),
                        topicName = any(),
                        anchor = any()
                    )
                } returns messages
                val actual = chatActor.execute(
                    ChatCommand.LoadAllMessages(
                        topicName = topicName,
                        streamName = streamName
                    )
                ).single()

                Then("all messages are loaded") {
                    val expected = ChatEvent.Internal.AllMessagesLoaded(messages)
                    actual shouldBe expected
                }
            }

            And("loading is failed") {
                coEvery {
                    loadMessagesInTopicUseCase.invoke(
                        streamName = any(),
                        topicName = any(),
                        anchor = any()
                    )
                } throws Exception()
                val actual = chatActor.execute(
                    ChatCommand.LoadAllMessages(
                        topicName = topicName,
                        streamName = streamName
                    )
                ).single()

                Then("all messages are not loaded") {
                    val expected = ChatEvent.Internal.Error
                    actual shouldBe expected
                }
            }
        }

        When("command is LoadCachedMessages") {
            And("loading is successful") {
                coEvery {
                    loadAllCachedMessagesUseCase.invoke(
                        topicName = any(),
                        streamName = any()
                    )
                } returns messages
                val actual = chatActor.execute(
                    ChatCommand.LoadAllCachedMessages(
                        topicName = topicName,
                        streamName = streamName
                    )
                ).single()

                Then("all cached messages are loaded") {
                    val expected = ChatEvent.Internal.AllCachedMessagesLoaded(messages)
                    actual shouldBe expected
                }
            }

            And("loading is failed") {
                coEvery {
                    loadAllCachedMessagesUseCase.invoke(
                        topicName = any(),
                        streamName = any()
                    )
                } throws Exception()
                val actual = chatActor.execute(
                    ChatCommand.LoadAllCachedMessages(
                        topicName = topicName,
                        streamName = streamName
                    )
                ).single()

                Then("all cached messages are not loaded") {
                    val expected = ChatEvent.Internal.ErrorCached
                    actual shouldBe expected
                }
            }
        }

        When("command is SendMessage") {
            And("sending is successful") {
                coEvery {
                    sendMessageUseCase.invoke(
                        content = any(),
                        topicName = any(),
                        streamId = any()
                    )
                } returns message.id
                coEvery {
                    loadSelectedMessageUseCase.invoke(
                        messageId = any()
                    )
                } returns message
                val actual = chatActor.execute(
                    ChatCommand.SendMessage(
                        content = "content",
                        topicName = topicName,
                        streamId = topic.streamId
                    )
                )

                Then("message is sent") {
                    val expected = ChatEvent.Internal.MessageSent(message)
                    actual.single() shouldBe expected
                }
            }

            And("sending is failed") {
                coEvery {
                    sendMessageUseCase.invoke(
                        content = any(),
                        topicName = any(),
                        streamId = any()
                    )
                } throws Exception()
                coEvery {
                    loadSelectedMessageUseCase.invoke(
                        messageId = any()
                    )
                } throws Exception()
                val actual = chatActor.execute(
                    ChatCommand.SendMessage(
                        content = "content",
                        topicName = topicName,
                        streamId = topic.streamId
                    )
                )

                Then("message is sent") {
                    val expected = ChatEvent.Internal.ErrorMessage
                    actual.single() shouldBe expected
                }
            }
        }

        When("command is FetchMoreMessages") {
            And("sending is successful") {
                coEvery {
                    loadMessagesInTopicUseCase.invoke(
                        streamName = any(), topicName = any(), anchor = any()
                    )
                } returns messages
                val actual = chatActor.execute(
                    ChatCommand.FetchMoreMessages(
                        topicName = topicName,
                        streamName = streamName,
                        anchor = MessageAnchor.Message(1)
                    )
                )

                Then("messages is fetched") {
                    val expected = ChatEvent.Internal.OldMessagesLoaded(messages)
                    actual.single() shouldBe expected
                }
            }

            And("sending is failed") {
                coEvery {
                    loadMessagesInTopicUseCase.invoke(
                        streamName = any(), topicName = any(), anchor = any()
                    )
                } throws Exception()
                val actual = chatActor.execute(
                    ChatCommand.FetchMoreMessages(
                        topicName = topicName,
                        streamName = streamName,
                        anchor = MessageAnchor.Message(1)
                    )
                )

                Then("messages is fetched") {
                    val expected = ChatEvent.Internal.Error
                    actual.single() shouldBe expected
                }
            }
        }
    }
})