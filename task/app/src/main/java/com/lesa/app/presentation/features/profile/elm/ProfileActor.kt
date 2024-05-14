package com.lesa.app.presentation.features.profile.elm

import com.lesa.app.domain.use_cases.profile.LoadProfileUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ProfileActor @Inject constructor(
    private val loadProfileUseCase: LoadProfileUseCase
) : Actor<ProfileCommand, ProfileEvent>() {
    override fun execute(command: ProfileCommand): Flow<ProfileEvent> {
        return when(command) {
            is ProfileCommand.LoadData -> flow {
                emit(loadProfileUseCase.invoke())
            }.mapEvents(
                eventMapper = {
                    ProfileEvent.Internal.DataLoaded(profile = it)
                },
                errorMapper = {
                    ProfileEvent.Internal.Error
                }
            )
        }
    }
}