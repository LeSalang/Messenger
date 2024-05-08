package com.lesa.app.presentation.features.chat.message

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.lesa.app.App
import com.lesa.app.R
import com.lesa.app.databinding.MessageViewBinding
import com.lesa.app.di.NetworkModule.Companion.BASE_URL
import com.lesa.app.presentation.features.chat.message.emoji.EmojiFlexBox
import com.lesa.app.presentation.features.chat.message.emoji.EmojiView
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MessageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    attachToRoot: Boolean = true,
) : ViewGroup(context, attributeSet, defStyleAttr, defStyleRes) {
    private var binding: MessageViewBinding

    @Inject
    lateinit var picasso: Picasso

    private var model: Model? = null

    private val logoCard: View
        get() = binding.messageUserLogoCard

    private val logoImageView: ImageView
        get() = binding.messageUserLogo

    private val nameTextView: TextView
        get() = binding.messageUserName

    private val messageTextView: TextView
        get() = binding.messageText

    private val emojiFlexBox: EmojiFlexBox
        get() = binding.emojiFlexBox

    private val linearLayout: View
        get() = binding.linearLayout

    private val textCard: CardView
        get() = binding.messageTextCardView

    init {
        App.INSTANCE.appComponent.inject(this)
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.message_view, this, attachToRoot)
        binding = MessageViewBinding.bind(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val originalWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val width = (originalWidth * MESSAGE_WIDTH_FACTOR).toInt()

        linearLayout.measure(
            MeasureSpec.makeMeasureSpec(
                width - logoCard.measuredWidth, MeasureSpec.EXACTLY
            ), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        val height =
            maxOf(linearLayout.measuredHeight, logoCard.measuredHeight) + paddingTop + paddingBottom
        setMeasuredDimension(originalWidth, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val logoCardLeft = if (model?.type == Model.Type.INCOMING) {
            paddingLeft
        } else {
            r - l - logoCard.measuredWidth - linearLayout.measuredWidth - paddingLeft - paddingRight
        }

        logoCard.layout(
            logoCardLeft,
            paddingTop,
            logoCardLeft + logoCard.measuredWidth,
            paddingTop + logoCard.measuredHeight
        )

        linearLayout.layout(
            logoCardLeft + logoCard.measuredWidth,
            paddingTop,
            r,
            paddingTop + linearLayout.measuredHeight
        )
    }

    fun update(
        model: Model,
        actions: Actions
    ) {
        val imageGetter = TextViewImageGetter(
            textView = messageTextView,
            baseUrl = BASE_URL,
            picasso = picasso
        )
        if (this.model?.avatar != model.avatar) {
            Picasso.get().load(model.avatar).into(logoImageView)
        }
        if (this.model?.userName != model.userName) {
            nameTextView.text = model.userName
        }
        if (this.model?.text != model.text) {
            messageTextView.text = Html.fromHtml(
                model.text,
                Html.FROM_HTML_MODE_LEGACY,
                imageGetter,
                null
            ).trimEnd()
        }
        if (this.model?.emojiList != model.emojiList) {
            emojiFlexBox.emojiList = model.emojiList
            emojiFlexBox.isGone = emojiFlexBox.emojiList.isEmpty()
        }
        when (model.type) {
            Model.Type.INCOMING -> {
                logoCard.visibility = VISIBLE
                nameTextView.visibility = VISIBLE
                textCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gray_28))
            }

            Model.Type.OUTGOING -> {
                logoCard.visibility = INVISIBLE
                nameTextView.visibility = GONE
                textCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cyan))
            }
        }
        this.model = model
        textCard.setOnLongClickListener {
            actions.onLongClick(model.id)
            true
        }
        emojiFlexBox.addEmojiClickListener { emojiCode ->
            actions.onEmojiClick(model.id, emojiCode)
        }
        emojiFlexBox.addPlusButtonClickListener {
            actions.onPlusButtonClick(model.id)
        }
    }

    data class Model(
        val id: Int,
        val avatar: String?,
        val userName: String,
        val text: String,
        val emojiList: List<EmojiView.Model>,
        val type: Type,
    ) {
        enum class Type {
            INCOMING, OUTGOING
        }
    }

    data class Actions(
        val onLongClick: (Int) -> Unit,
        val onEmojiClick: (Int, String) -> Unit,
        val onPlusButtonClick: (Int) -> Unit,
    )

    companion object {
        private const val MESSAGE_WIDTH_FACTOR = 0.8
    }
}