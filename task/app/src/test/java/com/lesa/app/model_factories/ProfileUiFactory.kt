package com.lesa.app.model_factories

import com.lesa.app.presentation.features.profile.model.ProfileUi

object ProfileUiFactory {
    fun create(
        id: Int = 1,
        name: String = "name",
        email: String = "email",
        avatar: String = "avatar"
    ): ProfileUi {
        return ProfileUi(
            id = id,
            name = name,
            email = email,
            avatar = avatar
        )
    }
}