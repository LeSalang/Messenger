package com.lesa.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lesa.app.customView.EmojiView
import com.lesa.app.customView.MessageView
import com.lesa.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val emojiList = mutableListOf<EmojiView.Model>()
    private var avatarIndex = 0
    private lateinit var message: MessageView.Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addPresetEmojis()
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
            messageView.addEmojiClickListener(this@MainActivity::onEmojiClicked)
        }
    }

    private fun updateAvatar() {
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
}}