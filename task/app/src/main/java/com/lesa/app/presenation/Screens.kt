package com.lesa.app.presenation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.lesa.app.domain.model.Topic
import com.lesa.app.domain.model.User
import com.lesa.app.presenation.channels.ChannelsFragment
import com.lesa.app.presenation.chat.ChatFragment
import com.lesa.app.presenation.main.MainFragment
import com.lesa.app.presenation.people.PeopleFragment
import com.lesa.app.presenation.another_profile.AnotherProfileFragment
import com.lesa.app.presenation.profile.ProfileFragment

object Screens {
    fun AnotherProfile(user: User) = FragmentScreen {
        AnotherProfileFragment.getNewInstance(user)
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