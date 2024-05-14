

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.lesa.app.presentation.features.profile.ProfileFragment
import mocks.ApiMockServer.Companion.api
import mocks.UsersMock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileTests : TestCase() {

    @get:Rule
    val wireMockRule = WireMockRule(8080)

    @Test
    fun testProfile() {
        wireMockRule.api {
            mock(mock = UsersMock.GetOwnUser)
        }
        val profileFragmentRule = launchFragmentInContainer<ProfileFragment>()
        ProfileScreen {
            userNameTextView {
                isVisible()
                containsText("Lev")
            }
        }
    }
}