package com.lesa.app.presentation.features.profile

import com.lesa.app.domain.model.User
import com.lesa.app.presentation.features.profile.model.ProfileMapper
import com.lesa.app.presentation.features.profile.model.ProfileUi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class ProfileMapperTest : BehaviorSpec({

    Given("profile mapper") {
        val user = User(
            id = 1,
            name = "name",
            email = "email",
            avatar = "avatar",
            presence = User.Presence(
                status = User.Presence.Status.ACTIVE,
                timestamp = Date(1_000_000_000)
            )
        )

        When("profile mapper is called") {
            val actual = ProfileMapper.map(user)

            Then("profile is mapped") {
                val expected = ProfileUi(
                    id = 1,
                    name = "name",
                    email = "email",
                    avatar = "avatar"
                )
                actual shouldBe expected
            }
        }
    }
})