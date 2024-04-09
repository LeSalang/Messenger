package com.lesa.app.chat.emoji_picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.databinding.ItemEmojiBinding
import com.lesa.app.model.EmojiCNCS

class EmojiPickerAdapter(
    private val emojiList: List<EmojiCNCS>,
    private val onSelect: (EmojiCNCS) -> Unit,
) : RecyclerView.Adapter<EmojiPickerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            ItemEmojiBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), onSelect
        )
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(emojiList[position])
    }

    class ViewHolder(_binding: ItemEmojiBinding, val onSelect: (EmojiCNCS) -> Unit) :
        RecyclerView.ViewHolder(_binding.root) {
        private var binding = _binding

        fun bind(emoji: EmojiCNCS) {
            binding.emojiTextView.text = emoji.getCodeString()
            itemView.setOnClickListener {
                onSelect(emoji)
            }
        }
    }
}