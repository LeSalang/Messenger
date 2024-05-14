package com.lesa.app.data.repositories

import com.lesa.app.data.local.dao.StreamDao
import com.lesa.app.data.network.Api
import com.lesa.app.data.network.models.AllSubscribedStreamsApiDto
import com.lesa.app.model_factories.StreamApiDtoFactory
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StreamsRepositoryImplTest : BehaviorSpec({
    Given("streams repository") {
        val api: Api = mockk()
        val dao: StreamDao = mockk()
        val repository: StreamsRepository = StreamsRepositoryImpl(api, dao)

        When("get all streams") {
            coEvery {
                api.getAllSubscribedStreams()
            } returns AllSubscribedStreamsApiDto(List(10) { StreamApiDtoFactory.create() })
        }
    }
})