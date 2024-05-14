package com.lesa.app.presentation.features.people

import com.lesa.app.model_factories.UserFactory
import com.lesa.app.presentation.features.people.elm.PeopleCommand
import com.lesa.app.presentation.features.people.elm.PeopleEffect
import com.lesa.app.presentation.features.people.elm.PeopleEvent
import com.lesa.app.presentation.features.people.elm.PeopleReducer
import com.lesa.app.presentation.features.people.elm.PeopleState
import com.lesa.app.presentation.features.people.model.UserMapper
import com.lesa.app.presentation.utils.LceState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PeopleReducerTest : BehaviorSpec({

    Given("people reducer") {
        val reducer = PeopleReducer()
        val users = List(10) { UserFactory.create() }

        When("event is internal") {
            And("data is loaded") {
                val actual = reducer.reduce(
                    event = PeopleEvent.Internal.DataLoaded(users),
                    state = PeopleState(
                        lceState = LceState.Idle,
                        users = users,
                        isSearching = false
                    )
                )

                Then("status should changes to content") {
                    val expected = PeopleState(
                        lceState = LceState.Content(users.map { UserMapper.map(it) }),
                        users = users,
                        isSearching = false
                    )
                    actual.state shouldBe expected
                }
            }

            And("cached data is loaded") {

                And("loaded list is not empty") {
                    val actual = reducer.reduce(
                        event = PeopleEvent.Internal.CachedDataLoaded(users),
                        state = PeopleState(
                            lceState = LceState.Idle,
                            users = emptyList(),
                            isSearching = false
                        )
                    )

                    Then("status should changes to content and init command LoadUsers") {
                        val expected = PeopleState(
                            lceState = LceState.Content(users.map { UserMapper.map(it) }),
                            users = users,
                            isSearching = false
                        )
                        actual.state shouldBe expected
                        actual.commands.single() shouldBe PeopleCommand.LoadUsers
                    }
                }

                And("loaded list is empty") {
                    val actual = reducer.reduce(
                        event = PeopleEvent.Internal.CachedDataLoaded(emptyList()),
                        state = PeopleState(
                            lceState = LceState.Idle,
                            users = emptyList(),
                            isSearching = false
                        )
                    )

                    Then("status should changes to loading and init command LoadUsers") {
                        val expected = PeopleState(
                            lceState = LceState.Loading,
                            users = emptyList(),
                            isSearching = false
                        )
                        actual.state shouldBe expected
                        actual.commands.single() shouldBe PeopleCommand.LoadUsers
                    }
                }
            }

            And("data is not loaded") {

                And("state user is not empty") {
                    val actual = reducer.reduce(
                        event = PeopleEvent.Internal.Error,
                        state = PeopleState(
                            lceState = LceState.Content(users.map { UserMapper.map(it) }),
                            users = users,
                            isSearching = false
                        )
                    )

                    Then("status shouldn't changes") {
                        val expected = PeopleState(
                            lceState = LceState.Content(users.map { UserMapper.map(it) }),
                            users = users,
                            isSearching = false
                        )
                        actual.state shouldBe expected
                    }
                }

                And("state user is empty") {
                    val actual = reducer.reduce(
                        event = PeopleEvent.Internal.Error,
                        state = PeopleState(
                            lceState = LceState.Content(emptyList()),
                            users = emptyList(),
                            isSearching = false
                        )
                    )

                    Then("status shouldn't changes") {
                        val expected = PeopleState(
                            lceState = LceState.Error,
                            users = emptyList(),
                            isSearching = false
                        )
                        actual.state shouldBe expected
                    }
                }
            }

            And("cached data is not loaded") {
                val actual = reducer.reduce(
                    event = PeopleEvent.Internal.ErrorCached,
                    state = PeopleState(
                        lceState = LceState.Idle,
                        users = emptyList(),
                        isSearching = false
                    )
                )

                Then("status should changes to loading and init command LoadUsers") {
                    val expected = PeopleState(
                        lceState = LceState.Loading,
                        users = emptyList(),
                        isSearching = false
                    )
                    actual.state shouldBe expected
                    actual.commands.single() shouldBe PeopleCommand.LoadUsers
                }
            }
        }

        When("event is ui") {
            And("init") {
                val actual = reducer.reduce(
                    event = PeopleEvent.Ui.Init,
                    state = PeopleState(
                        lceState = LceState.Idle,
                        users = emptyList(),
                        isSearching = false
                    )
                )

                Then("command LoadCachedUsers should be init") {
                    actual.commands.single() shouldBe PeopleCommand.LoadCachedUsers
                }
            }

            And("reload people") {
                val actual = reducer.reduce(
                    event = PeopleEvent.Ui.ReloadPeople,
                    state = PeopleState(
                        lceState = LceState.Loading,
                        users = emptyList(),
                        isSearching = false
                    )
                )

                Then("command LoadUsers should be init") {
                    actual.commands.single() shouldBe PeopleCommand.LoadUsers
                }
            }

            And("search") {
                val actual = reducer.reduce(
                    event = PeopleEvent.Ui.Search(query = "false search"),
                    state = PeopleState(
                        lceState = LceState.Idle,
                        users = users,
                        isSearching = true
                    )
                )

                Then("status should changes to content") {
                    val expected = PeopleState(
                        lceState = LceState.Content(emptyList()),
                        users = users,
                        isSearching = true
                    )
                    actual.state shouldBe expected
                }
            }

            And("on search clicked") {
                val actual = reducer.reduce(
                    event = PeopleEvent.Ui.OnSearchClicked,
                    state = PeopleState(
                        lceState = LceState.Idle,
                        users = users,
                        isSearching = false
                    )
                )

                Then("status should changes to content") {
                    val expected = PeopleState(
                        lceState = LceState.Idle,
                        users = users,
                        isSearching = true
                    )
                    actual.state shouldBe expected
                }
            }

            And("profile opened") {
                val actual = reducer.reduce(
                    event = PeopleEvent.Ui.OpenProfile(UserMapper.map(UserFactory.create())),
                    state = PeopleState(
                        lceState = LceState.Content(users.map { UserMapper.map(it) }),
                        users = users,
                        isSearching = false
                    )
                )

                Then("effect OpenProfile should be init") {
                    val expected = PeopleEffect.OpenProfile(
                        UserMapper.map(UserFactory.create())
                    )
                    actual.effects.single() shouldBe expected
                }
            }
        }
    }
})