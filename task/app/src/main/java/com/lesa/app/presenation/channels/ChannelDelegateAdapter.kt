package com.lesa.app.presenation.channels

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
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
            binding.channelNameTextView.text = item.stream.name
            binding.channelExpandIcon.isSelected = item.isExpanded
            itemView.setOnClickListener {
                onClick(item.stream.id)
            }
            val color = item.stream.color?.let {
                Color.parseColor(it)
            } ?: itemView.resources.getColor(R.color.gray_18)
            val alfaColor = ColorUtils.setAlphaComponent(color, 100)
            binding.root.setBackgroundColor(alfaColor)
        }
    }
}