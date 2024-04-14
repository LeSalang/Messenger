package com.lesa.app.channels

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.composite_adapter.DelegateAdapter
import com.lesa.app.databinding.ItemTopicBinding
import com.lesa.app.model.Topic

class TopicDelegateAdapter (
    private val onClick: (Int) -> Unit
) :
    DelegateAdapter<TopicDelegateItem, TopicDelegateAdapter.ViewHolder>(TopicDelegateItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: TopicDelegateItem) {
        holder.bind(item.topic)
    }

    class ViewHolder(view: View, val onClick: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding = ItemTopicBinding.bind(itemView)

        fun bind(topic: Topic) {
            binding.topicNameTextView.text = topic.name
            val color = topic.color?.let {
                Color.parseColor(it)
            } ?: itemView.resources.getColor(R.color.gray_18)
            val alfaColor = ColorUtils.setAlphaComponent(color, 80)
            binding.root.setBackgroundColor(alfaColor)
            /*binding.topicMesCountTextView.text = String.format(
                itemView.context.getString(R.string.channels_mes_count_pattern),
                topic.count
            )
            itemView.setOnClickListener {
                onClick(topic.id)
            }*/
        }
    }
}