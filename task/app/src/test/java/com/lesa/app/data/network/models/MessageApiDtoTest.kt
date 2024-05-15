package com.lesa.app.data.network.models

import com.lesa.app.domain.model.Emoji
import com.lesa.app.domain.model.Message
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class MessageApiDtoTest : BehaviorSpec({
    Given("MessageApiDto") {
        val messageApiDto = MessageApiDto(
            id = 1,
            avatar = "https://null",
            content = "content",
            reactions = listOf(
                ReactionApiDto(emojiCode = "emojiCode1", emojiName = "emojiName1", userId = 1),
                ReactionApiDto(emojiCode = "emojiCode2", emojiName = "emojiName2", userId = 1),
                ReactionApiDto(emojiCode = "emojiCode2", emojiName = "emojiName2", userId = 2),
            ),
            senderId = 2,
            senderFullName = "John",
            topic = "topic",
            timestampSecs = 1715372754
        )
        When("map to Message") {
            val actual = messageApiDto.toMessage(ownId = 2)
            Then("should be equal") {
                val expected = Message(
                    id = 1,
                    senderAvatar = "https://null",
                    content = "content",
                    senderName = "John",
                    reactions = mapOf(
                        "emojiCode1" to Emoji(
                            emojiCode = "emojiCode1",
                            emojiName = "emojiName1",
                            isOwn = false,
                            count = 1
                        ),
                        "emojiCode2" to Emoji(
                            emojiCode = "emojiCode2",
                            emojiName = "emojiName2",
                            isOwn = true,
                            count = 2
                        )
                    ),
                    date = Date(1715372754000),
                    topic = "topic",
                    isOwn = true
                )
                actual shouldBe expected
            }
        }
    }
})