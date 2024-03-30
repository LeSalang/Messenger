package com.lesa.app.chat.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.compositeAdapter.DelegateAdapter
import com.lesa.app.databinding.MessageItemBinding

class MessageDelegateAdapter:
    DelegateAdapter<MessageDelegateItem, MessageDelegateAdapter.ViewHolder> (MessageDelegateItem::class.java) {

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.message_item, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, item: MessageDelegateItem) {
            holder.bind(
                item.message
            )
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val binding = MessageItemBinding.bind(itemView)

            fun bind(
                model: MessageView.Model
            ) {
                binding.message.apply {
                    update(model)
                }
            }
        }
}