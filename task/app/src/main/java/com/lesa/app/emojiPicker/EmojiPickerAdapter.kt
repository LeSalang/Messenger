package com.lesa.app.emojiPicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.databinding.EmojiItemBinding

class EmojiPickerAdapter(private val emojiList: List<String>): RecyclerView.Adapter<EmojiPickerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
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
        return holder.bind(emojiList[position])
    }

    class ViewHolder(_binding: EmojiItemBinding) : RecyclerView.ViewHolder(_binding.root) {
        private var binding = EmojiItemBinding.bind(itemView)

        fun bind(emoji: String) {
            binding.emojiTextView.text = emoji
        }



    }


}