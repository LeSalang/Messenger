package com.lesa.app.presentation.features.people.elm

import com.lesa.app.domain.model.User
import com.lesa.app.domain.use_cases.people.LoadPeopleUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class PeopleActor(
    private val loadPeopleUseCase: LoadPeopleUseCase
) : Actor<PeopleCommand, PeopleEvent>() {
    private var userList = listOf<User>()
    override fun execute(command: PeopleCommand): Flow<PeopleEvent> {
        return when (command) {
            is PeopleCommand.LoadData -> flow {
                runCatching {
                    loadPeopleUseCase.invoke()
                }.fold(
                    onSuccess = { userList ->
                        this@PeopleActor.userList = userList
                        emit(
                            PeopleEvent.Internal.DataLoaded(
                                userList = this@PeopleActor.userList
                            )
                        )
                    },
                    onFailure = {
                        emit(PeopleEvent.Internal.Error)
                    }
                )
            }
            is PeopleCommand.Search -> flow {
                runCatching {
                    search(command.query)
                }.fold(
                    onSuccess = {
                        emit(
                            PeopleEvent.Internal.DataLoaded(
                                userList = it
                            )
                        )
                    },
                    onFailure = {
                        emit(PeopleEvent.Internal.Error)
                    }
                )
            }
        }
    }

    private fun search(query: String): List<User> {
        val refactoredQuery = query.trim(' ')
        if (refactoredQuery.isEmpty()) return userList
        val list = userList.filter { user ->
            user.name.contains(refactoredQuery, ignoreCase = true)
                || user.name.contains(refactoredQuery, ignoreCase = true)
        }
        return list
    }
}