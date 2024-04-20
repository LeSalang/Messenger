package com.lesa.app.presentation.features.people.elm

import com.lesa.app.domain.use_cases.people.LoadPeopleUseCase
import com.lesa.app.presentation.features.people.model.UserMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class PeopleActor(
    private val loadPeopleUseCase: LoadPeopleUseCase
) : Actor<PeopleCommand, PeopleEvent>() {
    override fun execute(command: PeopleCommand): Flow<PeopleEvent> {
        return when (command) {
            is PeopleCommand.LoadData -> flow {
                runCatching {
                    loadPeopleUseCase.invoke()
                }.fold(
                    onSuccess = { userList ->
                        emit(PeopleEvent.Internal.DataLoaded(userUiList = userList.map { user ->
                            UserMapper().map(user)
                        })
                        )
                    },
                    onFailure = {
                        emit(PeopleEvent.Internal.Error)
                    }
                )
            }
        }
    }
}