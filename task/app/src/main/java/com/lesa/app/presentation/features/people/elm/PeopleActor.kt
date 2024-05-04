package com.lesa.app.presentation.features.people.elm

import com.lesa.app.domain.use_cases.people.LoadCachedPeopleUseCase
import com.lesa.app.domain.use_cases.people.LoadPeopleUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class PeopleActor @Inject constructor(
    private val loadPeopleUseCase: LoadPeopleUseCase,
    private val loadCachedPeopleUseCase: LoadCachedPeopleUseCase
) : Actor<PeopleCommand, PeopleEvent>() {
    override fun execute(command: PeopleCommand): Flow<PeopleEvent> {
        return when (command) {
            PeopleCommand.LoadUsers -> flow {
                emit(loadPeopleUseCase.invoke())
            }.mapEvents(
                eventMapper = { userList ->
                    PeopleEvent.Internal.DataLoaded(
                        users = userList
                    )
                },
                errorMapper = {
                    PeopleEvent.Internal.Error
                }
            )

            PeopleCommand.LoadCachedUsers -> flow {
                emit(loadCachedPeopleUseCase.invoke())
            }.mapEvents (
                eventMapper = { userList ->
                    PeopleEvent.Internal.CachedDataLoaded(
                        users = userList
                    )
                },
                errorMapper = {
                    PeopleEvent.Internal.ErrorCached
                }
            )
        }
    }
}