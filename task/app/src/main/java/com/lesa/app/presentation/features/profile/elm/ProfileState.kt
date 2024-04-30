package com.lesa.app.presentation.features.profile.elm

import com.lesa.app.presentation.features.profile.model.ProfileUi
import com.lesa.app.presentation.utils.LceState

data class ProfileState(
    val profileUi: LceState<ProfileUi>
)