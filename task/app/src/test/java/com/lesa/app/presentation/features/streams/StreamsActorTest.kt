package com.lesa.app.presentation.features.streams

import com.lesa.app.domain.use_cases.streams.CreateStreamUseCase
import com.lesa.app.domain.use_cases.streams.LoadCachedStreamsUseCase
import com.lesa.app.domain.use_cases.streams.LoadStreamsUseCase
import com.lesa.app.model_factories.StreamFactory
import com.lesa.app.presentation.features.streams.elm.StreamsActor
import com.lesa.app.presentation.features.streams.elm.StreamsCommand
import com.lesa.app.presentation.features.streams.elm.StreamsEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StreamsActorTest : BehaviorSpec({

    Given("stream actor") {
        val loadStreamsUseCase: LoadStreamsUseCase = mockk()
        val loadCachedStreamsUseCase: LoadCachedStreamsUseCase = mockk()
        val creteStreamUseCase: CreateStreamUseCase = mockk()
        val streamsActor = StreamsActor(loadStreamsUseCase, loadCachedStreamsUseCase, creteStreamUseCase)
        val streams = List(5) { StreamFactory.create() }

        When("command is LoadStreams") {
            And("loading is successful") {
                coEvery {
                    loadStreamsUseCase.invoke()
                } returns streams
                val actual = streamsActor.execute(StreamsCommand.LoadStreams).single()

                Then("streams are loaded") {
                    val expected = StreamsEvent.Internal.DataLoaded(streams = streams)
                    actual shouldBe expected
                }
            }

            And("loading is failed") {
                coEvery {
                    loadStreamsUseCase.invoke()
                } throws Exception()
                val actual = streamsActor.execute(StreamsCommand.LoadStreams).single()

                Then("streams aren't loaded") {
                    val expected = StreamsEvent.Internal.Error
                    actual shouldBe expected
                }
            }
        }

        When("command is LoadCachedStreams") {
            And("loading is successful") {
                coEvery {
                    loadCachedStreamsUseCase.invoke()
                } returns streams
                val actual = streamsActor.execute(StreamsCommand.LoadCachedStreams).single()

                Then("streams are loaded") {
                    val expected = StreamsEvent.Internal.CachedDataLoaded(streams = streams)
                    actual shouldBe expected
                }
            }

            And("loading is failed") {
                coEvery {
                    loadCachedStreamsUseCase.invoke()
                } throws Exception()
                val actual = streamsActor.execute(StreamsCommand.LoadCachedStreams).single()

                Then("streams aren't loaded") {
                    val expected = StreamsEvent.Internal.ErrorCached
                    actual shouldBe expected
                }
            }
        }

        When("command is CreateStream") {
            val newStream = StreamFactory.create(name = "new stream")
            val newStreams = streams + newStream
            And("creating is successful") {
                coEvery {
                    creteStreamUseCase.invoke(any())
                } returns newStreams
                val actual = streamsActor.execute(StreamsCommand.CreateStream("streamName")).single()

                Then("streams are created") {
                    val expected = StreamsEvent.Internal.DataLoaded(streams = newStreams)
                    actual shouldBe expected
                }
            }

            And("network request is failed") {
                coEvery {
                    creteStreamUseCase.invoke(any())
                } throws Exception()
                val actual = streamsActor.execute(StreamsCommand.CreateStream("streamName")).single()

                Then("stream aren't created") {
                    val expected = StreamsEvent.Internal.CreateStreamError
                    actual shouldBe expected
                }
            }
        }
    }
})