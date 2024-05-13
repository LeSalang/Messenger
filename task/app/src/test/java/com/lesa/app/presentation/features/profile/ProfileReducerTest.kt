package com.lesa.app.presentation.features.profile

import com.lesa.app.model_factories.UserFactory
import com.lesa.app.presentation.features.profile.elm.ProfileCommand
import com.lesa.app.presentation.features.profile.elm.ProfileEvent
import com.lesa.app.presentation.features.profile.elm.ProfileReducer
import com.lesa.app.presentation.features.profile.elm.ProfileState
import com.lesa.app.presentation.features.profile.model.ProfileMapper
import com.lesa.app.presentation.utils.LceState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProfileReducerTest : BehaviorSpec({
    Given("ProfileReducer") {
        val reducer = ProfileReducer()

        When("reduce") {
            And("internal event") {
                And("data is loaded") {
                    val user = UserFactory.create()
                    val actual = reducer.reduce(
                        event = ProfileEvent.Internal.DataLoaded(profile = user),
                        state = ProfileState(profileUi = LceState.Idle)
                    )

                    Then("should change status to content") {
                        val expected = ProfileState(profileUi = LceState.Content(ProfileMapper.map(user = user)))
                        actual.state shouldBe expected
                    }
                }

                And("data is not loaded") {
                    val actual = reducer.reduce(
                        event = ProfileEvent.Internal.Error,
                        state = ProfileState(profileUi = LceState.Idle)
                    )

                    Then("should change status to error") {
                        val expected = ProfileState(profileUi = LceState.Error)
                        actual.state shouldBe expected
                    }
                }
            }

            And("external event") {
                And("reload profile") {
                    val actual = reducer.reduce(
                        event = ProfileEvent.Ui.ReloadProfile,
                        state = ProfileState(profileUi = LceState.Idle)
                    )

                    Then("should init command load data") {
                        val expected = ProfileCommand.LoadData
                        actual.commands shouldContain expected
                    }
                }

                And("init") {
                    val actual = reducer.reduce(
                        event = ProfileEvent.Ui.Init,
                        state = ProfileState(profileUi = LceState.Idle)
                    )

                    Then("should init command load data") {
                        val expected = ProfileCommand.LoadData
                        actual.commands shouldContain expected
                    }
                }
            }
        }
    }
})