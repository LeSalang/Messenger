package com.lesa.app.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lesa.app.R
import com.lesa.app.databinding.MessageViewBinding
import com.squareup.picasso.Picasso

class MessageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
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

    var emojiList: List<EmojiView.Model>
        get() = emojiFlexBox.emojiList
        set(value) {
            emojiFlexBox.emojiList = value
        }

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.message_view, this, true)
        binding = MessageViewBinding.bind(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        linearLayout.measure(
            MeasureSpec.makeMeasureSpec(width - logoCard.measuredWidth - paddingRight, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        val height = maxOf(linearLayout.measuredHeight, logoCard.measuredHeight) + paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        logoCard.layout(
            paddingLeft,
            paddingTop,
            paddingLeft + logoCard.measuredWidth,
            paddingTop + logoCard.measuredHeight
        )

        linearLayout.layout(
            paddingLeft + logoCard.measuredHeight,
            paddingTop,
            r,
            b
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
        this.model = model
    }

    fun addEmojiClickListener(onClick: (String) -> Unit) {
        emojiFlexBox.addClickListener(onClick)
    }

    data class Model(
        val avatar: Int,
        val userName: String,
        val text: String
    )
}