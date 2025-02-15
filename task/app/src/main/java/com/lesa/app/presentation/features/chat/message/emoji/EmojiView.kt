package com.lesa.app.presentation.features.chat.message.emoji

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.view.setPadding
import com.lesa.app.R

class EmojiView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(
    context, attributeSet, defStyleAttr, defStyleRes
) {
    var emoji = Model("", 0, false)
        set(value) {
            if (value != field) {
                field = value
                isSelected = value.isSelected
                requestLayout()
            }
        }

    private val textToDraw: String
        get() = if (emoji.count > 0) {
            "${emoji.emoji} ${emoji.count}"
        } else {
            emoji.emoji
        }

    private val textPaint = TextPaint().apply {
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        color = resources.getColor(
            if (isDarkTheme) R.color.gray_223_alfa_100 else R.color.gray_31_alfa_100
        )
        textSize = resources.getDimension(R.dimen.text_size_small_x)
    }

    private val textRect = Rect()

    init {
        setPadding(
            resources.getDimensionPixelOffset(R.dimen.space_size_medium)
        )
        setBackgroundResource(R.drawable.emoji_bg)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)

        val actualWidth =
            resolveSize(textRect.width() + paddingLeft + paddingRight, widthMeasureSpec)
        val actualHeight =
            resolveSize(textRect.height() + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val topOffset = height / 2f - textRect.exactCenterY()
        canvas.drawText(textToDraw, paddingLeft.toFloat(), topOffset, textPaint)
    }

    data class Model(
        val emojiCode: String,
        val count: Int,
        val isSelected: Boolean,
    ) {
        val emoji: String
            get() {
                return runCatching {
                    val codeParts = emojiCode
                        .split("-")
                        .map { it.toInt(16) }
                        .toIntArray()
                    String(codeParts, 0, codeParts.size)
                }.getOrElse { "💩" }
            }
    }
}