package com.lesa.app.presentation.profile

import com.lesa.app.presentation.profile.model.ProfileUi
import com.lesa.app.presentation.utils.ScreenState

data class ProfileState(
    val profileUi: ScreenState<ProfileUi>
)