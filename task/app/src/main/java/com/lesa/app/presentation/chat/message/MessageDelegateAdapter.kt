package com.lesa.app.presentation.chat.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.composite_adapter.DelegateAdapter
import com.lesa.app.databinding.ItemMessageBinding

class MessageDelegateAdapter(
    private val actions: MessageView.Actions,
) :
    DelegateAdapter<MessageDelegateItem, MessageDelegateAdapter.ViewHolder>(MessageDelegateItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(
            view = view,
            actions = actions
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, item: MessageDelegateItem) {
        holder.bind(
            item.message
        )
    }

    class ViewHolder(
        view: View,
        private val actions: MessageView.Actions,
    ) : RecyclerView.ViewHolder(view) {
        private val binding = ItemMessageBinding.bind(itemView)
        fun bind(
            model: MessageView.Model,
        ) {
            binding.message.apply {
                update(
                    model = model,
                    actions = actions
                )
            }
        }
    }
}