package com.lesa.app.presentation.features.people.elm

import com.lesa.app.domain.model.User
import com.lesa.app.domain.use_cases.people.LoadPeopleUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class PeopleActor @Inject constructor(
    private val loadPeopleUseCase: LoadPeopleUseCase
) : Actor<PeopleCommand, PeopleEvent>() {
    private var userList = listOf<User>()
    override fun execute(command: PeopleCommand): Flow<PeopleEvent> {
        return when (command) {
            is PeopleCommand.LoadData -> flow {
                emit(loadPeopleUseCase.invoke())
            }.mapEvents(
                eventMapper = { userList ->
                    this@PeopleActor.userList = userList
                    PeopleEvent.Internal.DataLoaded(
                        userList = this@PeopleActor.userList
                    )
                },
                errorMapper = {
                    PeopleEvent.Internal.Error
                }
            )

            is PeopleCommand.Search -> flow {
                emit(search(command.query))
            }.mapEvents(
                eventMapper = {
                    PeopleEvent.Internal.DataLoaded(
                        userList = it
                    )
                },
                errorMapper = {
                    PeopleEvent.Internal.Error
                }
            )
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