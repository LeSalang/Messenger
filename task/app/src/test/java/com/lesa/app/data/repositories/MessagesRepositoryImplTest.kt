package com.lesa.app.data.repositories

import android.content.ContentResolver
import android.net.Uri
import com.lesa.app.data.local.dao.MessageDao
import com.lesa.app.data.local.entities.MessageEntity
import com.lesa.app.data.local.entities.toMessage
import com.lesa.app.data.network.Api
import com.lesa.app.data.network.models.AllMessagesApiDto
import com.lesa.app.data.network.models.MessageResponseApiDto
import com.lesa.app.data.network.models.SendMessageResponseApiDto
import com.lesa.app.data.network.models.UploadFileResponseApiDto
import com.lesa.app.data.network.models.toMessage
import com.lesa.app.domain.model.MessageAnchor
import com.lesa.app.model_factories.MessageApiDtoFactory
import com.lesa.app.model_factories.MessageEntityFactory
import com.lesa.app.model_factories.UserApiDtoFactory
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MessagesRepositoryImplTest : BehaviorSpec({
    Given("messages repository") {
        val api: Api = mockk()
        val dao: MessageDao = mockk()
        val repository: MessagesRepository = MessagesRepositoryImpl(api = api, dao = dao)

        When("get messages in topic") {
            val messageAnchor = MessageAnchor.Message(id = 1)
            val messagesApiDto = List(20) { MessageApiDtoFactory.create() }
            val userId = 1

            coEvery {
                api.getOwnUser()
            } returns UserApiDtoFactory.create(id = userId)

            coEvery {
                api.getAllMessagesInStream(narrow = any(), anchor = any())
            } returns AllMessagesApiDto(messages = messagesApiDto)

            coEvery {
                dao.getAll()
            } returns List(40) { MessageEntityFactory.create() }

            coEvery {
                dao.deleteAllInTopic(topicName = any())
            } just Runs

            val cachedMessagesSlot = slot<List<MessageEntity>>()
            coEvery {
                dao.updateMessages(messages = capture(cachedMessagesSlot))
            } just Runs

            val actual = repository.getMessagesInTopic("Stream name", "topic name", messageAnchor)

            Then("should be equal") {
                val expected = messagesApiDto.map { it.toMessage(ownId = userId) }
                actual shouldBe expected
            }

            Then("should cache no more than 50 messages") {
                cachedMessagesSlot.captured.size shouldBe 50
            }
        }

        When("get all cached messages in topic") {
            val topicName = "topic2"
            val cachedMessages = listOf(
                MessageEntityFactory.create(topic = "topic1"),
                MessageEntityFactory.create(topic = topicName),
                MessageEntityFactory.create(topic = topicName),
                MessageEntityFactory.create(topic = "topic3")
            )

            coEvery {
                dao.getAll()
            } returns cachedMessages

            val actual = repository.getAllCachedMessagesInTopic(topicName)

            Then("should be equal") {
                val expected = listOf(cachedMessages[1], cachedMessages[2]).map { it.toMessage() }
                actual shouldBe expected
            }
        }

        When("get one message by Id") {
            val messageId = 1
            val userId = 1

            coEvery {
                api.getOwnUser()
            } returns UserApiDtoFactory.create(id = userId)

            coEvery {
                api.getMessage(messageId = messageId)
            } returns MessageResponseApiDto(message = MessageApiDtoFactory.create(id = messageId, senderId = userId))

            val actual = api.getMessage(messageId = messageId).message.toMessage(ownId = userId)

            Then("should be equal") {
                val expected = MessageApiDtoFactory.create(id = messageId, senderId = userId).toMessage(ownId = userId)
                actual shouldBe expected
            }
        }

        When("upload file") {
            coEvery {
                api.uploadFile(any())
            } returns UploadFileResponseApiDto(uri = "uri")
            val uri: Uri = mockk()
            val contentResolver = mockk<ContentResolver>()
            repository.uploadFile(
                uri = uri,
                contentResolver = contentResolver
            )

            Then("api method is called") {
                coVerify {
                    api.uploadFile(any())
                }
            }
        }

        When("send message") {
            coEvery {
                api.sendMessage(
                    content = "content",
                    topicName = "topic name",
                    streamId = 1
                )
            } returns SendMessageResponseApiDto(id = 1)
            val actual = repository.sendMessage(content = "content", topicName = "topic name", streamId = 1)

            Then("should be equal") {
                val expected = 1
                actual shouldBe expected
            }
        }
    }
})