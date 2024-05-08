package com.lesa.app.presentation.features.chat.message

import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.net.URI

class TextViewImageGetter (
    private val textView: TextView,
    private val baseUrl: String,
    private val picasso: Picasso
) : Html.ImageGetter {

    override fun getDrawable(source: String): Drawable {
        val drawable = TextViewImage(textView)

        val isAbsolute = source.startsWith("http")

        val url = if (isAbsolute) URI.create(source) else {
            URI.create(baseUrl).resolve(source)
        }

        if (url == null) {
            return drawable
        }

        picasso.load(url.toString()).into(drawable)

        return drawable
    }
}