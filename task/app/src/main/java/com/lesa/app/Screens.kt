package com.lesa.app

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.lesa.app.channels.ChannelsFragment
import com.lesa.app.chat.ChatFragment
import com.lesa.app.people.PeopleFragment
import com.lesa.app.profile.AnotherProfileFragment
import com.lesa.app.profile.ProfileFragment

object Screens {
    fun AnotherProfile(userId: Int) = FragmentScreen {
        AnotherProfileFragment.getNewInstance(userId)
    }

    fun Channels() = FragmentScreen {
        ChannelsFragment()
    }

    fun Chat(topicId: Int) = FragmentScreen {
        ChatFragment.getNewInstance(topicId)
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