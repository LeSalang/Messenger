package com.lesa.app.presentation.features.chat

import com.lesa.app.model_factories.MessageFactory
import com.lesa.app.model_factories.StreamFactory
import com.lesa.app.model_factories.TopicFactory
import com.lesa.app.presentation.features.chat.elm.ChatCommand
import com.lesa.app.presentation.features.chat.elm.ChatEffect
import com.lesa.app.presentation.features.chat.elm.ChatEvent
import com.lesa.app.presentation.features.chat.elm.ChatReducer
import com.lesa.app.presentation.features.chat.elm.ChatState
import com.lesa.app.presentation.features.chat.message_context_menu.MessageContextMenuAction
import com.lesa.app.presentation.features.chat.models.ChatMapper
import com.lesa.app.presentation.utils.LceState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ChatReducerTest : BehaviorSpec({

    Given("chat reducer") {
        val reducer = ChatReducer()
        val message = MessageFactory.create()
        val messageList = List(10) { message }
        val messageUiList = messageList.map{
            ChatMapper.map(
                message = it,
                needShowTopic = false
            )
        }
        val topicName = "topic"
        val streamName = "stream"
        val topic = TopicFactory.create(name = topicName)
        val stream = StreamFactory.create(
            name = streamName,
            topics = List(2) { topic }
        )

        When("event is internal") {
            And("all messages are loaded") {
                val actual = reducer.reduce(
                    event = ChatEvent.Internal.AllMessagesLoaded(messages = messageList),
                    state = ChatState(
                        lceState = LceState.Idle,
                        messages = messageList,
                        topic = topic,
                        stream = stream,
                        isPrefetching = false
                    )
                )

                Then("status should changes to content") {
                    val expected = ChatState(
                        lceState = LceState.Content(messageUiList),
                        messages = messageList,
                        topic = topic,
                        stream = stream,
                        isPrefetching = false
                    )
                    actual.state shouldBe expected
                }
            }

            And("all cached messages are loaded") {
                And("events list of messages is empty") {
                    val actual = reducer.reduce(
                        event = ChatEvent.Internal.AllCachedMessagesLoaded(messages = emptyList()),
                        state = ChatState(
                            lceState = LceState.Idle,
                            messages = emptyList(),
                            topic = topic,
                            stream = stream,
                            isPrefetching = false
                        )
                    )

                    Then("status should changes to loading") {
                        val expected = ChatState(
                            lceState = LceState.Loading,
                            messages = emptyList(),
                            topic = topic,
                            stream = stream,
                            isPrefetching = false
                        )
                        actual.state shouldBe expected
                    }
                }

                And("events list of messages isn't empty") {
                    val actual = reducer.reduce(
                        event = ChatEvent.Internal.AllCachedMessagesLoaded(messages = messageList),
                        state = ChatState(
                            lceState = LceState.Idle,
                            messages = messageList,
                            topic = topic,
                            stream = stream,
                            isPrefetching = false
                        )
                    )

                    Then("status should changes to loading") {
                        val expected = ChatState(
                            lceState = LceState.Content(messageUiList),
                            messages = messageList,
                            topic = topic,
                            stream = stream,
                            isPrefetching = false
                        )
                        actual.state shouldBe expected
                    }
                }
            }

            And("error and list is empty") {
                val actual = reducer.reduce(
                    event = ChatEvent.Internal.Error,
                    state = ChatState(
                        lceState = LceState.Idle,
                        messages = emptyList(),
                        topic = topic,
                        stream = stream,
                        isPrefetching = false
                    )
                )

                Then("status should changes to loading") {
                    val expected = ChatState(
                        lceState = LceState.Error,
                        messages = emptyList(),
                        topic = topic,
                        stream = stream,
                        isPrefetching = false
                    )
                    actual.state shouldBe expected
                }
            }

            And("error cached") {
                val actual = reducer.reduce(
                    event = ChatEvent.Internal.ErrorCached,
                    state = ChatState(
                        lceState = LceState.Idle,
                        messages = emptyList(),
                        topic = topic,
                        stream = stream,
                        isPrefetching = false
                    )
                )

                Then("status should changes to loading and invoke command LoadAllMessages") {
                    val expectedState = ChatState(
                        lceState = LceState.Loading,
                        messages = emptyList(),
                        topic = topic,
                        stream = stream,
                        isPrefetching = false
                    )
                    actual.state shouldBe expectedState
                    actual.commands.first() shouldBe ChatCommand.LoadAllMessages(
                        topicName = topicName,
                        streamName = streamName
                    )
                }
            }
        }

        When("event is UI") {
            And("action button clicked") {
                And("content is blank") {
                    val actual = reducer.reduce(
                        event = ChatEvent.Ui.ActionButtonClicked(
                            content = "",
                            topicName = topicName
                        ),
                        state = ChatState(
                            lceState = LceState.Idle,
                            messages = emptyList(),
                            topic = topic,
                            stream = stream,
                            isPrefetching = false
                        )
                    )

                    Then("effect ShowAttachmentsPicker should be invoked") {
                        actual.effects.first() shouldBe ChatEffect.ShowAttachmentsPicker
                    }
                }

                And("content isn't blank") {
                    val actual = reducer.reduce(
                        event = ChatEvent.Ui.ActionButtonClicked(
                            content = "123",
                            topicName = topicName
                        ),
                        state = ChatState(
                            lceState = LceState.Idle,
                            messages = emptyList(),
                            topic = topic,
                            stream = stream,
                            isPrefetching = false
                        )
                    )

                    Then("effect ShowAttachmentsPicker should be invoked") {
                        actual.commands.first() shouldBe ChatCommand.SendMessage(
                            content = "123",
                            topicName = topicName,
                            streamId = stream.id
                        )
                    }
                }
            }

            And("select menu action") {
                And("add reaction") {
                    val actual = reducer.reduce(
                        event = ChatEvent.Ui.SelectMenuAction(
                            messageId = message.id,
                            action = MessageContextMenuAction.ADD_REACTION
                        ),
                        state = ChatState(
                            lceState = LceState.Idle,
                            messages = messageList,
                            topic = topic,
                            stream = stream,
                            isPrefetching = false
                        )
                    )

                    Then("effect ShowEmojiPicker should be invoked") {
                        actual.effects.first() shouldBe ChatEffect.ShowEmojiPicker(message.id)
                    }
                }

                And("delete message") {
                    val actual = reducer.reduce(
                        event = ChatEvent.Ui.SelectMenuAction(
                            messageId = message.id,
                            action = MessageContextMenuAction.DELETE_MESSAGE
                        ),
                        state = ChatState(
                            lceState = LceState.Idle,
                            messages = messageList,
                            topic = topic,
                            stream = stream,
                            isPrefetching = false
                        )
                    )

                    Then("command DeleteMessage should be invoked") {
                        actual.commands.first() shouldBe ChatCommand.DeleteMessage(message.id)
                    }
                }

                And("change topic") {
                    val actual = reducer.reduce(
                        event = ChatEvent.Ui.SelectMenuAction(
                            messageId = message.id,
                            action = MessageContextMenuAction.CHANGE_TOPIC
                        ),
                        state = ChatState(
                            lceState = LceState.Idle,
                            messages = messageList,
                            topic = topic,
                            stream = stream,
                            isPrefetching = false
                        )
                    )

                    Then("effect ShowChangeTopic should be invoked") {
                        actual.effects.first() shouldBe ChatEffect.ShowChangeTopicDialog(
                            stream = stream, messageId = message.id)
                    }
                }
            }
        }
    }
})