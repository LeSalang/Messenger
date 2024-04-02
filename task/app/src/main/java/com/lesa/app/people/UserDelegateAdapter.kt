package com.lesa.app.people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.composite_adapter.DelegateAdapter
import com.lesa.app.databinding.ItemPeopleBinding
import com.lesa.app.model.User
import com.squareup.picasso.Picasso

class UserDelegateAdapter (
    private val onClick: (Int) -> Unit
) : DelegateAdapter<UserDelegateItem, UserDelegateAdapter.ViewHolder>(UserDelegateItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_people, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: UserDelegateItem) {
        holder.bind(item.user)
    }

    class ViewHolder(view: View, val onClick: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding = ItemPeopleBinding.bind(itemView)

        fun bind(user: User) {
            Picasso.get().load(user.avatar).into(binding.peopleLogoImage)
            binding.peopleNameTextView.text = user.name
            binding.peopleEmailTextView.text = user.email
            itemView.setOnClickListener {
                onClick(user.id)
            }
            // TODO: set netIndicatorColor
        }
    }
}
