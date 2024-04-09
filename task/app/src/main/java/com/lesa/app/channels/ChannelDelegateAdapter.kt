package com.lesa.app.channels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.composite_adapter.DelegateAdapter
import com.lesa.app.databinding.ItemChannelBinding

class ChannelDelegateAdapter (
    private val onClick: (Int) -> Unit
) :
    DelegateAdapter<ChannelDelegateItem, ChannelDelegateAdapter.ViewHolder>(ChannelDelegateItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: ChannelDelegateItem) {
        holder.bind(item)
    }

    class ViewHolder(view: View, val onClick: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding = ItemChannelBinding.bind(itemView)

        fun bind(item: ChannelDelegateItem) {
            binding.channelNameTextView.text = item.channel.name
            binding.channelExpandIcon.isSelected = item.isExpanded
            itemView.setOnClickListener {
                onClick(item.channel.id)
            }
        }
    }
}