package com.lesa.app.presentation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.lesa.app.domain.model.Topic
import com.lesa.app.presentation.channels.ChannelsFragment
import com.lesa.app.presentation.chat.ChatFragment
import com.lesa.app.presentation.main.MainFragment
import com.lesa.app.presentation.features.people.PeopleFragment
import com.lesa.app.presentation.features.people.model.UserUi
import com.lesa.app.presentation.features.user.UserFragment
import com.lesa.app.presentation.features.profile.ProfileFragment

object Screens {
    fun AnotherProfile(user: UserUi) = FragmentScreen {
        UserFragment.getNewInstance(user)
    }

    fun Channels() = FragmentScreen {
        ChannelsFragment()
    }

    fun Chat(topic: Topic) = FragmentScreen {
        ChatFragment.getNewInstance(topic)
    }

    fun Main() = FragmentScreen {
        MainFragment()
    }

    fun People() = FragmentScreen {
        PeopleFragment()
    }

    fun Profile() = FragmentScreen {
        ProfileFragment()
    }
}