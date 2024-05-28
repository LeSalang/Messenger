package com.lesa.app.presentation.features.people

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.databinding.ItemPeopleBinding
import com.lesa.app.presentation.composite_adapter.DelegateAdapter
import com.lesa.app.presentation.features.people.model.UserUi
import com.squareup.picasso.Picasso

class UserDelegateAdapter (
    private val onClick: (UserUi) -> Unit
) : DelegateAdapter<UserDelegateItem, UserDelegateAdapter.ViewHolder>(UserDelegateItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_people, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: UserDelegateItem) {
        holder.bind(item.user)
    }

    class ViewHolder(view: View, val onClick: (UserUi) -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding = ItemPeopleBinding.bind(itemView)

        fun bind(user: UserUi) {
            Picasso.get().load(user.avatar).into(binding.peopleLogoImage)
            binding.peopleNameTextView.text = user.name
            binding.peopleEmailTextView.text = user.email
            itemView.setOnClickListener {
                onClick(user)
            }
            if (user.presence.color != null) {
                binding.peopleNetStatusIndicator.visibility = View.VISIBLE
                binding.peopleNetStatusIndicator.imageTintList =
                    ColorStateList.valueOf(itemView.resources.getColor(user.presence.color))
            } else {
                binding.peopleNetStatusIndicator.visibility = View.GONE
            }
        }
    }
}
