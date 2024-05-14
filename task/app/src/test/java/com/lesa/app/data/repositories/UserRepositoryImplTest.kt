package com.lesa.app.data.repositories

import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.local.entities.toUser
import com.lesa.app.data.network.Api
import com.lesa.app.data.network.models.AllPresenceApiDto
import com.lesa.app.data.network.models.AllUsersApiDto
import com.lesa.app.data.network.models.toPresence
import com.lesa.app.data.network.models.toUser
import com.lesa.app.model_factories.PresenceApiDtoFactory
import com.lesa.app.model_factories.UserApiDtoFactory
import com.lesa.app.model_factories.UserEntityFactory
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserRepositoryImplTest : BehaviorSpec({
    Given("user repository") {
        val api: Api = mockk()
        val dao: UserDao = mockk()
        val repository: UserRepository = UserRepositoryImpl(api, dao)

        When("get updated users") {
            val presences = mapOf(
                "mail1" to PresenceApiDtoFactory.create(),
                "mail2" to PresenceApiDtoFactory.create(),
                "mail3" to PresenceApiDtoFactory.create()
            )
            val userApiDtos = List(10) { UserApiDtoFactory.create() }
            val users = userApiDtos.map { it.toUser(presence = presences[it.email].toPresence()) }

            coEvery {
                api.getAllUsersPresence()
            } returns AllPresenceApiDto(presences)

            coEvery {
                api.getAllUsers()
            } returns AllUsersApiDto(userApiDtos)

            coEvery {
                dao.updateUsers(any())
            } just Runs

            val actual = repository.getUpdatedUsers()

            Then("should be equal") {
                actual shouldBe users
            }

            Then("all messages cached") {
                coVerify(exactly = 1) {
                    dao.updateUsers(any())
                }
            }
        }

        When("get cached users") {
            val userEntities = List(10) { UserEntityFactory.create() }
            coEvery {
                dao.getAll()
            } returns userEntities
            val actual = repository.getCachedUsers()

            Then("should be equal") {
                val expected = userEntities.map { it.toUser() }
                actual shouldBe expected
            }
        }
    }
})