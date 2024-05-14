package com.lesa.app.data.local.entities

import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.Topic
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StreamEntityTest : BehaviorSpec({
    Given("StreamEntity") {

        And("with color") {
            val streamEntity = StreamEntity(
                id = 1,
                name = "streamName",
                color = "#111111",
                isSubscribed = true,
                topics = listOf("topic1", "topic2", "topic3")
            )

            When("map to Stream") {
                val actual = streamEntity.toStream()

                Then("should be equal") {
                    val expected = Stream(
                        id = 1,
                        name = "streamName",
                        isSubscribed = true,
                        color = "#111111",
                        topics = listOf(
                            Topic(
                                name = "topic1",
                                color = "#111111",
                                streamName = "streamName",
                                streamId = 1
                            ),
                            Topic(
                                name = "topic2",
                                color = "#111111",
                                streamName = "streamName",
                                streamId = 1
                            ),
                            Topic(
                                name = "topic3",
                                color = "#111111",
                                streamName = "streamName",
                                streamId = 1
                            )
                        )
                    )
                    actual shouldBe expected
                }
            }
        }

        And("without color") {
            val streamEntity = StreamEntity(
                id = 1,
                name = "streamName",
                color = null,
                isSubscribed = true,
                topics = listOf("topic1", "topic2", "topic3")
            )

            When("map to Stream") {
                val actual = streamEntity.toStream()

                Then("should be equal") {
                    val expected = Stream(
                        id = 1,
                        name = "streamName",
                        isSubscribed = true,
                        color = null,
                        topics = listOf(
                            Topic(
                                name = "topic1",
                                color = "#464646",
                                streamName = "streamName",
                                streamId = 1
                            ),
                            Topic(
                                name = "topic2",
                                color = "#464646",
                                streamName = "streamName",
                                streamId = 1
                            ),
                            Topic(
                                name = "topic3",
                                color = "#464646",
                                streamName = "streamName",
                                streamId = 1
                            )
                        )
                    )
                    actual shouldBe expected
                }
            }
        }
    }
})