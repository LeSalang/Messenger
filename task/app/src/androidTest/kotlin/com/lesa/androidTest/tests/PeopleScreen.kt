package com.lesa.androidTest.tests

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.lesa.app.R
import com.lesa.app.presentation.features.people.PeopleFragment
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import org.hamcrest.Matcher

object PeopleScreen : KScreen<PeopleScreen>() {
    override val layoutId: Int = R.layout.fragment_people
    override val viewClass: Class<*> = PeopleFragment::class.java

    val recycler = KRecyclerView(
        builder = { withId(R.id.peopleRecyclerView) },
        itemTypeBuilder = { itemType { KPeopleItem(it) } }
    )

    class KPeopleItem(matcher: Matcher<View>) : KRecyclerItem<KPeopleItem>(matcher) {
    }
}