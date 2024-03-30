package com.lesa.app.emoji_picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.databinding.EmojiItemBinding
import com.lesa.app.model.EmojiCNCS

class EmojiPickerAdapter(private val emojiList: List<EmojiCNCS>) :
    RecyclerView.Adapter<EmojiPickerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            EmojiItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(emojiList[position].getCodeString())
    }

    class ViewHolder(_binding: EmojiItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        private var binding = _binding

        fun bind(emoji: String) {
            binding.emojiTextView.text = emoji
        }
    }
}