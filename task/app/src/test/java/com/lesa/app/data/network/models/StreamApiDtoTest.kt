package com.lesa.app.data.network.models

import com.lesa.app.domain.model.Stream
import com.lesa.app.domain.model.Topic
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StreamApiDtoTest : BehaviorSpec({
    Given("StreamApiDto") {
        val subscribedStreams: Map<Int, StreamApiDto> = mapOf(
            1 to StreamApiDto(id = 1, name = "streamName1", color = "#111111"),
            2 to StreamApiDto(id = 2, name = "streamName2", color = "#222222"),
            3 to StreamApiDto(id = 3, name = "streamName3", color = null)
        )
        And("from subscribed") {
            val topics: List<Topic> = listOf(
                Topic(name = "topicName1", color = "#111111", streamName = "streamName1", streamId = 1),
                Topic(name = "topicName2", color = "#111111", streamName = "streamName1", streamId = 1),
                Topic(name = "topicName3", color = "#111111", streamName = "streamName1", streamId = 1)
            )
            val streamApiDto = StreamApiDto(id = 1, name = "streamName1", color = "#111111")
            When("map to Stream") {
                val actual = streamApiDto.toStream(
                    subscribedStreams = subscribedStreams,
                    topics = topics
                )
                Then("should be equal") {
                    val expected = Stream(
                        id = 1,
                        name = "streamName1",
                        isSubscribed = true,
                        topics = listOf(
                            Topic(name = "topicName1", color = "#111111", streamName = "streamName1", streamId = 1),
                            Topic(name = "topicName2", color = "#111111", streamName = "streamName1", streamId = 1),
                            Topic(name = "topicName3", color = "#111111", streamName = "streamName1", streamId = 1)
                        ),
                        color = "#111111"
                    )
                    actual shouldBe expected
                }
            }
        }
        And("out of subscribed") {
            val topics: List<Topic> = listOf(
                Topic(name = "topicName1", color = "#444444", streamName = "streamName4", streamId = 4),
                Topic(name = "topicName2", color = "#444444", streamName = "streamName4", streamId = 4),
                Topic(name = "topicName3", color = "#444444", streamName = "streamName4", streamId = 4)
            )
            val streamApiDto = StreamApiDto(id = 4, name = "streamName4", color = "#444444")
            When("map to Stream") {
                val actual = streamApiDto.toStream(
                    subscribedStreams = subscribedStreams,
                    topics = topics
                )
                Then("should be equal") {
                    val expected = Stream(
                        id = 4,
                        name = "streamName4",
                        isSubscribed = false,
                        topics = listOf(
                            Topic(name = "topicName1", color = "#444444", streamName = "streamName4", streamId = 4),
                            Topic(name = "topicName2", color = "#444444", streamName = "streamName4", streamId = 4),
                            Topic(name = "topicName3", color = "#444444", streamName = "streamName4", streamId = 4)
                        ),
                        color = "#464646" // const color for unsubscribed streams in StreamApiModels file
                    )
                    actual shouldBe expected
                }
            }
        }
    }
})