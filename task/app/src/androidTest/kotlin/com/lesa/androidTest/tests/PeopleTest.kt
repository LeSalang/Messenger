package com.lesa.androidTest.tests
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.lesa.androidTest.mocks.ApiMockServer.Companion.api
import com.lesa.androidTest.mocks.UsersMock
import com.lesa.app.R
import com.lesa.app.presentation.features.people.PeopleFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PeopleTest : TestCase() {

    @get:Rule
    val wireMockRule = WireMockRule(8080)

    @Test
    fun testPeople() {
        wireMockRule.api {
            mock(mock = UsersMock.GetAllUsers)
            mock(mock = UsersMock.GetAllUsersPresence)
        }
        launchFragmentInContainer<PeopleFragment>(themeResId = R.style.Theme_BaseComponents)
        PeopleScreen {
            recycler.childAt<PeopleScreen.KPeopleItem>(0) {
                isVisible()
            }
        }
    }
}