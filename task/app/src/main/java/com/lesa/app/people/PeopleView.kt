package com.lesa.app.people

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lesa.app.R
import com.lesa.app.databinding.ItemPeopleBinding
import com.lesa.app.model.UserNetStatus
import com.squareup.picasso.Picasso

class PeopleView(
    context: Context,
) : ConstraintLayout(context) {
    private var binding: ItemPeopleBinding
    private var model: Model? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.item_profile, this)
        binding = ItemPeopleBinding.bind(this)
    }

    private val logoImage: ImageView
        get() = binding.peopleLogoImage

    private val netStatusIndicator: ImageView
        get() = binding.peopleNetStatusIndicator

    private val nameTextView: TextView
        get() = binding.peopleNameTextView

    private val emailTextView: TextView
        get() = binding.peopleEmailTextView

    data class Model(
        val logo: Int,
        val name: String,
        val email: String,
        val netStatus: UserNetStatus,
    )

    fun update(model: Model) {
        if (this.model?.logo != model.logo) {
            Picasso.get().load(model.logo).into(logoImage)
        }
        if (this.model?.name != model.name) {
            nameTextView.text = model.name
        }
        if (this.model?.email != model.email) {
            emailTextView.text = model.email
        }
        if (this.model?.netStatus != model.netStatus) {
            netStatusIndicator.setBackgroundResource(model.netStatus.color)
        }
        this.model = model
    }
}