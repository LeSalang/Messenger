package com.lesa.app.chat

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.lesa.app.R

class EmojiFlexBox @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attributeSet, defStyleAttr, defStyleRes) {
    var emojiList: List<EmojiView.Model> = listOf()
        set(value) {
            if (field != value) {
                field = value
                updateViews()
                requestLayout()
            }
        }

    private val plusButton: ImageView = ImageView(context).apply {
        setBackgroundResource(R.drawable.emoji_selected_bg)
        setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.add, null))
    }

    private val sampleEmojiView: EmojiView = EmojiView(context).apply {
        this.emoji = EmojiView.Model("🐒", 1, true)
    }

    private var onEmojiClick: ((String) -> Unit)? = null

    private val interItemSpacing = resources.getDimensionPixelOffset(R.dimen.emoji_flex_box_interitem_spacing)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        sampleEmojiView.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        val sampleHeight = sampleEmojiView.measuredHeight
        removeView(sampleEmojiView)
        val count = childCount
        var xPos = paddingLeft
        var yPos = paddingTop
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        for (i in 0..< count) {
            val child = getChildAt(i)
            child.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                childHeightMeasureSpec
            )
            if (xPos + child.measuredWidth > width) {
                xPos = paddingLeft
                yPos += sampleHeight + interItemSpacing
            }
            xPos += child.measuredWidth + interItemSpacing
        }
        val height = yPos + sampleHeight + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = r - l - paddingLeft - paddingRight
        var xPos = paddingLeft
        var yPos = paddingTop
        for (i in 0 ..< childCount) {
            val child = getChildAt(i)
            val childw = child.measuredWidth
            val childh = sampleEmojiView.measuredHeight
            if (xPos + childw > width) {
                xPos = paddingLeft
                yPos += childh + interItemSpacing
            }
            child.layout(xPos, yPos, xPos + childw, yPos + childh)
            xPos += childw + interItemSpacing
        }
    }

    fun addClickListener(onClick: (String) -> Unit) {
        onEmojiClick = onClick
    }

    private fun updateViews() {
        removeAllViews()
        emojiList.forEach {
            val emojiView = EmojiView(context)
            emojiView.emoji = it
            emojiView.setOnClickListener { clickedView ->
                val emoji = (clickedView as EmojiView).emoji.emoji
                onEmojiClick?.invoke(emoji)
            }
            addView(emojiView)
        }
        addView(plusButton)
        addView(sampleEmojiView)
    }
}