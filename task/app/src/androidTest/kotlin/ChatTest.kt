
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.lesa.app.domain.model.Topic
import com.lesa.app.presentation.features.chat.ChatFragment
import junit.framework.TestCase
import mocks.ApiMockServer.Companion.api
import mocks.MessagesMock
import mocks.UsersMock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatTest : TestCase() {

    @get:Rule
    val wireMockRule = WireMockRule(8080)

    @Test
    fun testChat() {
        wireMockRule.api {
            mock(mock = MessagesMock.GetAllMessagesInStream)
            mock(mock = UsersMock.GetOwnUser)
            mock(mock = MessagesMock.SendMessage)
            mock(mock = MessagesMock.GetMessage)
        }
        launchFragmentInContainer<ChatFragment>(
            fragmentArgs = Bundle().apply {
                putParcelable("topic_key", Topic(
                    name = "swimming turtles",
                    color = "#000000",
                    streamName = "streamName",
                    streamId = 432915
                ))
            }
        )
        ChatScreen {
            topicTitle {
                containsText("swimming turtles")
            }
            recycler.childAt<ChatScreen.KMessageItem>(0) {
                isVisible()
            }
            editText {
                replaceText("kek")
            }
            sendButton {
                isVisible()
//                click()
            }
        }
    }
}