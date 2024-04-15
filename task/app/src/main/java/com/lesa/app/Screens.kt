package com.lesa.app

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.lesa.app.channels.ChannelsFragment
import com.lesa.app.chat.ChatFragment
import com.lesa.app.model.Topic
import com.lesa.app.model.User
import com.lesa.app.people.PeopleFragment
import com.lesa.app.profile.AnotherProfileFragment
import com.lesa.app.profile.ProfileFragment

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