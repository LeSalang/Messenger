package com.lesa.app.presentation.features.people

import com.lesa.app.domain.use_cases.people.LoadCachedPeopleUseCase
import com.lesa.app.domain.use_cases.people.LoadPeopleUseCase
import com.lesa.app.model_factories.UserFactory
import com.lesa.app.presentation.features.people.elm.PeopleActor
import com.lesa.app.presentation.features.people.elm.PeopleCommand
import com.lesa.app.presentation.features.people.elm.PeopleEvent
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PeopleActorTest : BehaviorSpec({

    Given("people actor") {
        val loadPeopleUseCase: LoadPeopleUseCase = mockk()
        val loadCachedPeopleUseCase: LoadCachedPeopleUseCase = mockk()
        val peopleActor = PeopleActor(loadPeopleUseCase, loadCachedPeopleUseCase)
        val users = List(10) { UserFactory.create() }

        When("command is LoadUsers") {
            And("loading is successful") {
                coEvery {
                    loadPeopleUseCase.invoke()
                } returns users
                val actual = peopleActor.execute(PeopleCommand.LoadUsers).single()

                Then("users are loaded") {
                    val expected = PeopleEvent.Internal.DataLoaded(users = users)
                    actual shouldBe expected
                }
            }

            And("loading is failed") {
                coEvery {
                    loadPeopleUseCase.invoke()
                } throws Exception()
                val actual = peopleActor.execute(PeopleCommand.LoadUsers).single()

                Then("users are not loaded") {
                    val expected = PeopleEvent.Internal.Error
                    actual shouldBe expected
                }
            }
        }

        When("command is LoadCachedUsers") {
            And("loading is successful") {
                coEvery {
                    loadCachedPeopleUseCase.invoke()
                } returns users
                val actual = peopleActor.execute(PeopleCommand.LoadCachedUsers).single()

                Then("users are loaded") {
                    val expected = PeopleEvent.Internal.CachedDataLoaded(users = users)
                    actual shouldBe expected
                }
            }

            And("loading is failed") {
                coEvery {
                    loadCachedPeopleUseCase.invoke()
                } throws Exception()
                val actual = peopleActor.execute(PeopleCommand.LoadCachedUsers).single()

                Then("users are not loaded") {
                    val expected = PeopleEvent.Internal.ErrorCached
                    actual shouldBe expected
                }
            }
        }
    }
})