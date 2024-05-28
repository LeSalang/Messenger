package com.lesa.app.presentation.features.streams

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.databinding.ItemChannelBinding
import com.lesa.app.presentation.composite_adapter.DelegateAdapter

class StreamDelegateAdapter (
    private val onArrowClick: (Int) -> Unit,
    private val onStreamClick: (String) -> Unit
) :
    DelegateAdapter<StreamDelegateItem, StreamDelegateAdapter.ViewHolder>(StreamDelegateItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_channel, parent, false)
        return ViewHolder(view, onArrowClick = onArrowClick, onStreamClick = onStreamClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: StreamDelegateItem) {
        holder.bind(item)
    }

    class ViewHolder(
        view: View,
        val onArrowClick: (Int) -> Unit,
        val onStreamClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val binding = ItemChannelBinding.bind(itemView)

        fun bind(item: StreamDelegateItem) {
            binding.channelNameTextView.text = item.stream.name
            binding.channelExpandIcon.isSelected = item.isExpanded
            binding.channelExpandIcon.setOnClickListener {
                onArrowClick(item.stream.id)
            }
            binding.root.setOnClickListener {
                onStreamClick(item.stream.name)
            }
            val color = item.stream.color?.runCatching {
                Color.parseColor(this)
            }?.getOrNull() ?: itemView.resources.getColor(R.color.gray_15_alfa_100)
            val alfaColor = ColorUtils.setAlphaComponent(color, 100)
            binding.root.setBackgroundColor(alfaColor)
        }
    }
}