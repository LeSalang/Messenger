package com.lesa.app.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lesa.app.emojiPicker.EmojiPickerBottomSheetFragment
import com.lesa.app.databinding.FragmentChatBinding
import com.lesa.app.stubMessageList

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MessageListAdapter

    private val emojiList = mutableListOf<EmojiView.Model>()
    private var avatarIndex = 0
    private lateinit var message: MessageView.Model

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MessageListAdapter(stubMessageList)
        binding.chatRecyclerView.adapter = adapter

        binding.setAvatarButton.setOnClickListener {
            EmojiPickerBottomSheetFragment().show(childFragmentManager,
                EmojiPickerBottomSheetFragment.TAG
            )
        }

        /*addPresetEmojis()
        message = MessageView.Model(
            R.drawable.cat_1,
            resources.getString(R.string.sample_name),
            resources.getString(R.string.sample_message)
        )
        binding.messageView.update(message)

        with(binding) {
            setAvatarButton.setOnClickListener { updateAvatar() }
            setNameButton.setOnClickListener { updateName() }
            setMessageButton.setOnClickListener { updateMessage() }
            addEmojiButton.setOnClickListener { addEmoji() }
            messageView.addEmojiClickListener(this@ChatFragment::onEmojiClicked)
        }*/

    }



    /*private fun updateAvatar() {
        val imageList = listOf(
            R.drawable.cat_1,
            R.drawable.cat_2,
            R.drawable.cat_3
        )
        avatarIndex = (avatarIndex + 1) % imageList.size
        message = message.copy(avatar = imageList[avatarIndex])
        binding.messageView.update(message)
    }

    private fun updateName() {
        val userName = binding.nameEditText.text.toString()
        message = message.copy(userName = userName)
        binding.messageView.update(message)
        binding.nameEditText.text.clear()
    }

    private fun updateMessage() {
        val text = binding.messageEditText.text.toString()
        message = message.copy(text = text)
        binding.messageView.update(message)
        binding.messageEditText.text.clear()
    }

    private fun updateEmojiList() {
        emojiList.sortByDescending { it.count }
        binding.messageView.emojiList = emojiList.toList()
    }

    private fun addEmoji() {
        val allEmojis = listOf(
            "🐒", "🦍", "🦧", "🦝", "🦄", "🦓", "🐷", "🐏", "🐑", "🐫", "🐿", "🐊", "🦕", "🦖", "🐟"
        )
        val randomEmoji = allEmojis.random()
        emojiList.indexOfFirst {
            it.emoji == randomEmoji
        }.also {
            if (it >= 0) {
                emojiList[it] = emojiList[it].copy(
                    count = emojiList[it].count + 1
                )
            } else {
                emojiList.add(EmojiView.Model(randomEmoji, 1, true))
            }
        }
        updateEmojiList()
    }

    private fun addPresetEmojis() {
        emojiList.add(EmojiView.Model("🐷", 1, false))
        emojiList.add(EmojiView.Model("🦖", 3, false))
        emojiList.add(EmojiView.Model("\uD83E\uDDA7", 5, false))
        emojiList.add(EmojiView.Model("\uD83D\uDC11", 7, false))
        updateEmojiList()
    }

    private fun onEmojiClicked(emoji: String) {
        val selectedEmojiIndex = emojiList.indexOfFirst {
            it.emoji == emoji
        }
        val selectedEmoji = emojiList[selectedEmojiIndex]
        if (selectedEmoji.isSelected) {
            if (selectedEmoji.count == 1) {
                emojiList.removeAt(selectedEmojiIndex)
            } else {
                emojiList[selectedEmojiIndex] = selectedEmoji.copy(
                    count = selectedEmoji.count - 1,
                    isSelected = false
                )
            }
        } else {
            emojiList[selectedEmojiIndex] = selectedEmoji.copy(
                count = selectedEmoji.count + 1,
                isSelected = true
            )
        }
        updateEmojiList()
    }*/

    companion object {
        const val TAG = "ChatFragment"
    }
}