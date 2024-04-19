package com.lesa.app.presentation.features.profile

import com.lesa.app.presentation.features.profile.model.ProfileUi
import com.lesa.app.presentation.utils.ScreenState

data class ProfileState(
    val profileUi: ScreenState<ProfileUi>
)