package com.lesa.app.data.network.models

import com.lesa.app.domain.model.User
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class UserApiDtoTest : BehaviorSpec({
    Given("UserApiDto") {
        And("no delivery email") {
            val userApiDto = UserApiDto(
                id = 5115,
                deliveryEmail = null,
                email = "arthur.garcia@example.com",
                name = "Krista Dennis",
                avatar = "https://null"
            )
            When("map to User") {
                val actual = userApiDto.toUser(presence = User.Presence(status = User.Presence.Status.ACTIVE, timestamp = Date(1715372754)))
                Then("should be equal") {
                    val expected = User(
                        id = 5115,
                        email = "arthur.garcia@example.com",
                        name = "Krista Dennis",
                        avatar = "https://null",
                        presence = User.Presence(status = User.Presence.Status.ACTIVE, timestamp = Date(1715372754))
                    )
                    actual shouldBe expected
                }
            }
        }
        And("with delivery email") {
            val userApiDto = UserApiDto(
                id = 5115,
                deliveryEmail = "@@@null",
                email = "arthur.garcia@example.com",
                name = "Krista Dennis",
                avatar = "https://null"
            )
            When("map to User") {
                val actual = userApiDto.toUser(presence = User.Presence(status = User.Presence.Status.ACTIVE, timestamp = Date(1715372754)))
                Then("should be equal") {
                    val expected = User(
                        id = 5115,
                        email = "@@@null",
                        name = "Krista Dennis",
                        avatar = "https://null",
                        presence = User.Presence(status = User.Presence.Status.ACTIVE, timestamp = Date(1715372754))
                    )
                    actual shouldBe expected
                }
            }
        }
    }
})