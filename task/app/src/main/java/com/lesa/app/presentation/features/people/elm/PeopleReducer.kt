package com.lesa.app.presentation.features.people.elm

import com.lesa.app.domain.model.User
import com.lesa.app.presentation.features.people.elm.PeopleEvent
import com.lesa.app.presentation.features.people.model.UserMapper
import com.lesa.app.presentation.utils.LceState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import com.lesa.app.presentation.features.people.elm.PeopleCommand as Command
import com.lesa.app.presentation.features.people.elm.PeopleEffect as Effect
import com.lesa.app.presentation.features.people.elm.PeopleEvent as Event
import com.lesa.app.presentation.features.people.elm.PeopleState as State

class PeopleReducer : ScreenDslReducer<Event, Event.Ui, Event.Internal, State, Effect, Command> (
    Event.Ui::class,
    Event.Internal::class
) {
    override fun Result.internal(event: PeopleEvent.Internal): Any {
        return when (event) {
            is PeopleEvent.Internal.DataLoaded -> state {
                val userUiList = event.users.sortedByDescending {
                    it.presence
                }.map {
                    UserMapper.map(it)
                }
                copy(
                    lceState = LceState.Content(userUiList),
                    users = event.users
                )
            }

            is PeopleEvent.Internal.CachedDataLoaded -> {
                state {
                    val userUiList = event.users.sortedByDescending {
                        it.presence
                    }.map {
                        UserMapper.map(it)
                    }
                    if (userUiList.isEmpty()) {
                        copy(
                            lceState = LceState.Loading
                        )
                    } else {
                        copy(
                            lceState = LceState.Content(userUiList), users = event.users
                        )
                    }
                }
                commands {
                    +Command.LoadUsers
                }
            }

            PeopleEvent.Internal.Error -> state {
                if (users.isEmpty()) {
                    copy(
                        lceState = LceState.Error
                    )
                } else {
                    return@state this
                }
            }

            PeopleEvent.Internal.ErrorCached -> {
                state {
                    copy(
                        lceState = LceState.Loading
                    )
                }
                commands {
                    +Command.LoadUsers
                }

            }
        }
    }

    override fun Result.ui(event: PeopleEvent.Ui): Any {
        return when (event) {
            PeopleEvent.Ui.Init -> commands {
                +Command.LoadCachedUsers
            }

            PeopleEvent.Ui.ReloadPeople -> commands {
                +Command.LoadUsers
            }

            is PeopleEvent.Ui.Search -> state {
                val resultList = search(users = state.users, query = event.query)
                    .map { UserMapper.map(it) }
                copy(
                    lceState = LceState.Content(resultList)
                )
            }

            PeopleEvent.Ui.OnSearchClicked -> state {
                copy(
                    isSearching = !isSearching
                )
            }

            is PeopleEvent.Ui.OpenProfile -> effects {
                +Effect.OpenProfile(user = event.user)
            }
        }
    }

    private fun search(
        users: List<User>,
        query: String
    ): List<User> {
        val refactoredQuery = query.trim(' ')
        if (refactoredQuery.isEmpty()) return users
        val list = users.filter { user ->
            user.name.contains(refactoredQuery, ignoreCase = true)
                || user.name.contains(refactoredQuery, ignoreCase = true)
        }
        return list
    }
}