package com.lesa.app.chat.message

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.lesa.app.R
import com.lesa.app.chat.message.emoji.EmojiFlexBox
import com.lesa.app.chat.message.emoji.EmojiView
import com.lesa.app.databinding.MessageViewBinding
import com.squareup.picasso.Picasso

class MessageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    attachToRoot: Boolean = true
) : ViewGroup(context, attributeSet, defStyleAttr, defStyleRes) {
    private var binding: MessageViewBinding

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
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.message_view, this, attachToRoot)
        binding = MessageViewBinding.bind(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val originalWidth = MeasureSpec.getSize(widthMeasureSpec)- paddingLeft - paddingRight
        val width = (originalWidth * 0.8).toInt()

        linearLayout.measure(
            MeasureSpec.makeMeasureSpec(
                width - logoCard.measuredWidth,
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        val height = maxOf(linearLayout.measuredHeight, logoCard.measuredHeight) + paddingTop + paddingBottom
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

    fun update(model: Model) {
        if (this.model?.avatar != model.avatar) {
            Picasso.get().load(model.avatar).into(logoImageView)
        }
        if (this.model?.userName != model.userName) {
            nameTextView.text = model.userName
        }
        if (this.model?.text != model.text) {
            messageTextView.text = model.text
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
            model.onLongClick.invoke()
            return@setOnLongClickListener true
        }
        emojiFlexBox.addEmojiClickListener(model.onEmojiClick)
        emojiFlexBox.addPlusButtonClickListener(model.onPlusButtonClick)
    }

    data class Model(
        val id: Int,
        val avatar: Int,
        val userName: String,
        val text: String,
        val emojiList: List<EmojiView.Model>,
        val type: Type,
        val onLongClick: () -> Unit,
        val onEmojiClick: (String) -> Unit,
        val onPlusButtonClick: () -> Unit
    ) {
        enum class Type {
            INCOMING,
            OUTGOING
        }
    }
}