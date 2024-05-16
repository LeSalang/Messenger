package com.lesa.app.data.local.entities

import com.lesa.app.domain.model.Emoji
import com.lesa.app.domain.model.Message
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class MessageEntityTest : BehaviorSpec({
    Given("MessageEntity") {
        val messageEntity = MessageEntity(
            id = 1,
            content = "content",
            senderName = "senderName",
            senderAvatar = "senderAvatar",
            timestampMillis = 1715372754,
            topicName = "topic",
            streamName = "stream",
            reactions = listOf(
                ReactionEntity(
                    emojiCode = "emojiCode1",
                    emojiName = "emojiName1",
                    count = 10,
                    isOwn = true
                ),
                ReactionEntity(
                    emojiCode = "emojiCode2",
                    emojiName = "emojiName2",
                    count = 1,
                    isOwn = false
                ),
                ReactionEntity(
                    emojiCode = "emojiCode3",
                    emojiName = "emojiName3",
                    count = 2,
                    isOwn = true
                )
            ),
            isOwn = true
        )

        When("map to Message") {
            val actual = messageEntity.toMessage()

            Then("should be equal") {
                val expected = Message(
                    id = 1,
                    content = "content",
                    senderAvatar = "senderAvatar",
                    senderName = "senderName",
                    reactions = mapOf(
                        "emojiCode1" to Emoji(
                            emojiCode = "emojiCode1",
                            emojiName = "emojiName1",
                            isOwn = true,
                            count = 10
                        ),
                        "emojiCode2" to Emoji(
                            emojiCode = "emojiCode2",
                            emojiName = "emojiName2",
                            isOwn = false,
                            count = 1
                        ),
                        "emojiCode3" to Emoji(
                            emojiCode = "emojiCode3",
                            emojiName = "emojiName3",
                            isOwn = true,
                            count = 2
                        )
                    ),
                    date = Date(1715372754),
                    topic = "topic",
                    isOwn = true
                )
                actual shouldBe expected
            }
        }
    }
})