package com.lesa.app.profile

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lesa.app.R
import com.lesa.app.databinding.ItemProfileBinding
import com.lesa.app.model.UserNetStatus
import com.squareup.picasso.Picasso

class UserView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
    attachToRoot: Boolean = true
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private var binding: ItemProfileBinding
    private var model: Model? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.item_profile, this, attachToRoot)
        binding = ItemProfileBinding.bind(this)
    }

    private val logoImage: ImageView
        get() = binding.userLogoImage

    private val nameTextView: TextView
        get() = binding.userNameTextView

    private val chatStatusTextView: TextView
        get() = binding.userChatStatusTextView

    private val netStatusTextView: TextView
        get() = binding.userNetStatusTextView

    data class Model(
        val logo: Int,
        val name: String,
        val chatStatus: String,
        val netStatus: UserNetStatus
    )

    fun update(model: Model) {
        if (this.model?.logo != model.logo) {
            Picasso.get().load(model.logo).into(logoImage)
        }
        if (this.model?.name != model.name) {
            nameTextView.text = model.name
        }
        if (this.model?.chatStatus != model.chatStatus) {
            chatStatusTextView.text = model.chatStatus
        }
        if (this.model?.netStatus != model.netStatus) {
            netStatusTextView.text = ContextCompat.getString(context, model.netStatus.text)
            val color = ContextCompat.getColor(context, model.netStatus.color)
            netStatusTextView.setTextColor(color)
        }
        this.model = model
    }


}