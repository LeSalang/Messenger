package com.lesa.app.presentation.features.profile

import com.lesa.app.domain.use_cases.profile.LoadProfileUseCase
import com.lesa.app.model_factories.UserFactory
import com.lesa.app.presentation.features.profile.elm.ProfileActor
import com.lesa.app.presentation.features.profile.elm.ProfileCommand
import com.lesa.app.presentation.features.profile.elm.ProfileEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProfileActorTest : BehaviorSpec({

    Given("a profile actor") {
        val loadProfileUseCase: LoadProfileUseCase = mockk()
        val actor = ProfileActor(loadProfileUseCase)

        When("command is LoadData") {

            And("loading is successful") {
                val user = UserFactory.create()
                coEvery {
                    loadProfileUseCase.invoke()
                } returns user
                val actual = actor.execute(ProfileCommand.LoadData).single()

                Then("user is loaded") {
                    val expected = ProfileEvent.Internal.DataLoaded(user)
                    actual shouldBe expected
                }
            }

            And("loading is failed") {
                val user = UserFactory.create()
                coEvery {
                    loadProfileUseCase.invoke()
                } throws Exception()
                val actual = actor.execute(ProfileCommand.LoadData).single()

                Then("user is loaded") {
                    val expected = ProfileEvent.Internal.Error
                    actual shouldBe expected
                }
            }
        }
    }
})