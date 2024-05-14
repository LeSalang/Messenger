
import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.lesa.app.R
import com.lesa.app.presentation.features.chat.ChatFragment
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ChatScreen : KScreen<ChatScreen>() {
    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val recycler = KRecyclerView(
        builder = { withId(R.id.chatRecyclerView) },
        itemTypeBuilder = { itemType { KMessageItem(it) } }
    )

    val errorImage = KImageView{ withId(R.id.errorImageView) }

    val topicTitle = KTextView { withId(R.id.topicName) }

    val editText = KEditText { withId(R.id.messageEditText) }

    val sendButton = KButton { withId(R.id.sendButton) }

    class KMessageItem(matcher: Matcher<View>) : KRecyclerItem<KMessageItem>(matcher) {
    }
}