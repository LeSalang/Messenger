package com.lesa.app.presentation.features.profile.elm

import com.lesa.app.domain.use_cases.profile.LoadProfileUseCase
import com.lesa.app.presentation.features.profile.model.ProfileMapper
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
                runCatching {
                    loadProfileUseCase.invoke()
                }.fold(
                    onSuccess = {
                        val mapper = ProfileMapper()
                        val profileUi = mapper.map(it)
                        emit(ProfileEvent.Internal.DataLoaded(profileUi = profileUi))
                    },
                    onFailure = {
                        emit(ProfileEvent.Internal.Error)
                    }
                )
            }
        }
    }
}