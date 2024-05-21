package com.lesa.androidTest.tests

import com.kaspersky.kaspresso.screens.KScreen
import com.lesa.app.R
import com.lesa.app.presentation.features.profile.ProfileFragment
import io.github.kakaocup.kakao.text.KTextView

object ProfileScreen : KScreen<ProfileScreen>() {
    override val layoutId: Int = R.layout.fragment_profile
    override val viewClass: Class<*> = ProfileFragment::class.java

    val userNameTextView = KTextView { withId(R.id.userNameTextView) }
}