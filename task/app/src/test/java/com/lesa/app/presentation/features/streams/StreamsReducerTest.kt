package com.lesa.app.presentation.features.streams

import com.lesa.app.model_factories.StreamFactory
import com.lesa.app.presentation.features.streams.elm.StreamsCommand
import com.lesa.app.presentation.features.streams.elm.StreamsEffect
import com.lesa.app.presentation.features.streams.elm.StreamsEvent
import com.lesa.app.presentation.features.streams.elm.StreamsReducer
import com.lesa.app.presentation.features.streams.elm.StreamsState
import com.lesa.app.presentation.features.streams.model.StreamType
import com.lesa.app.presentation.features.streams.model.StreamsMapper
import com.lesa.app.presentation.utils.LceState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StreamsReducerTest : BehaviorSpec({
    Given("StreamReducer") {
        val reducer = StreamsReducer()
        val streams = listOf(
            StreamFactory.create(
                name = "streamName1",
                isSubscribed = false
            ),
            StreamFactory.create(
                isSubscribed = true
            )
        )

        When("reduce") {
            And("internal event") {
                And("data is loaded") {
                    val actual = reducer.reduce(
                        event = StreamsEvent.Internal.DataLoaded(streams = streams),
                        state = StreamsState(
                            lceState = LceState.Idle,
                            streams = streams,
                            streamType = StreamType.SUBSCRIBED,
                            expandedChannelId = 1,
                        )
                    )

                    Then("should change status to content") {
                        val streamUiList = listOf(streams[1])
                            .map { StreamsMapper.map(it) }
                        val expected = StreamsState(
                            lceState = LceState.Content(streamUiList),
                            streams = streams,
                            streamType = StreamType.SUBSCRIBED,
                            expandedChannelId = 1,
                        )
                        actual.state shouldBe expected
                    }
                }

                And("stream creating is failed") {
                    val actual = reducer.reduce(
                        event = StreamsEvent.Internal.CreateStreamError,
                        state = StreamsState(
                            lceState = LceState.Idle,
                            streams = streams,
                            streamType = StreamType.SUBSCRIBED,
                            expandedChannelId = 1,
                        )
                    ).effects.single()

                    Then("effect ShowNewStreamError is triggered") {
                        val expected = StreamsEffect.ShowNewStreamError
                        actual shouldBe expected
                    }
                }

            }

            And("UI event") {
                And("create stream") {
                    And("stream is existing") {
                        val actual = reducer.reduce(
                            event = StreamsEvent.Ui.CreateStream(streamName = "streamName1"),
                            state = StreamsState(
                                lceState = LceState.Idle,
                                streams = streams,
                                streamType = StreamType.SUBSCRIBED,
                                expandedChannelId = 1,
                            )
                        ).effects.single()

                        Then("effect ShowStreamExistsError is triggered") {
                            val expected = StreamsEffect.ShowStreamExistsError(streamName = "streamName1")
                            actual shouldBe expected
                        }
                    }

                    And("stream isn't existing") {
                        val actual = reducer.reduce(
                            event = StreamsEvent.Ui.CreateStream(streamName = "streamName2"),
                            state = StreamsState(
                                lceState = LceState.Idle,
                                streams = streams,
                                streamType = StreamType.SUBSCRIBED,
                                expandedChannelId = 1,
                            )
                        ).commands.single()

                        Then("command StreamCreated is triggered") {
                            val expected = StreamsCommand.CreateStream(streamName = "streamName2")
                            actual shouldBe expected
                        }
                    }
                }
            }
        }
    }
})