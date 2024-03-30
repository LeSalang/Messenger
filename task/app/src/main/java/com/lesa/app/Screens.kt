package com.lesa.app

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.lesa.app.chat.ChatFragment
import com.lesa.app.profile.AnotherProfileFragment
import com.lesa.app.profile.ProfileFragment

object Screens {
    fun Profile() = FragmentScreen {
        ProfileFragment()
    }

    fun AnotherProfile() = FragmentScreen {
        AnotherProfileFragment()
    }

    fun Chat() = FragmentScreen {
        ChatFragment()
    }
}