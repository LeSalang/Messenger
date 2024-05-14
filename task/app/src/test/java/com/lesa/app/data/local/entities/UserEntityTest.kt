package com.lesa.app.data.local.entities

import com.lesa.app.domain.model.User
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class UserEntityTest : BehaviorSpec({

    Given("UserEntity") {

        And("with irregular presence status") {
            val userEntity = UserEntity(
                id = 1,
                name = "name",
                email = "@email",
                avatar = "https://avatar",
                lastActivity = 1715372754,
                status = -1
            )

            When("map to User") {
                val actual = userEntity.toUser()

                Then("should be equal") {
                    val expected = User(
                        id = 1,
                        name = "name",
                        email = "@email",
                        avatar = "https://avatar",
                        presence = User.Presence(status = User.Presence.Status.OFFLINE, timestamp = Date(1715372754))
                    )
                    actual shouldBe expected
                }
            }
        }

        And("with ACTIVE presence status") {
            val userEntity = UserEntity(
                id = 1,
                name = "name",
                email = "@email",
                avatar = "https://avatar",
                lastActivity = 1715372754,
                status = 0
            )

            When("map to User") {
                val actual = userEntity.toUser()

                Then("should be equal") {
                    val expected = User(
                        id = 1,
                        name = "name",
                        email = "@email",
                        avatar = "https://avatar",
                        presence = User.Presence(status = User.Presence.Status.ACTIVE, timestamp = Date(1715372754))
                    )
                    actual shouldBe expected
                }
            }
        }
    }
})