package com.lesa.app.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.chat.date.DateDelegateAdapter
import com.lesa.app.chat.message.MessageDelegateAdapter
import com.lesa.app.chat.message.MessageView
import com.lesa.app.compositeAdapter.CompositeAdapter
import com.lesa.app.compositeAdapter.DelegateAdapter
import com.lesa.app.compositeAdapter.DelegateItem
import com.lesa.app.databinding.FragmentChatBinding
import com.lesa.app.emojiPicker.EmojiPickerBottomSheetFragment
import com.lesa.app.model.Emoji
import com.lesa.app.model.EmojiCNCS
import com.lesa.app.model.Message
import com.lesa.app.model.emojiSetCNCS
import com.lesa.app.repositories.UserRepository
import com.lesa.app.stubMessageList
import java.util.Date

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CompositeAdapter
    private val messages = stubMessageList.toMutableList() // TODO: move to VM
    private val userRepository = UserRepository() // TODO: move to VM

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
        setUpViews()
    }

    private fun setUpViews() {
        setUpRecycleView()
        setUpSendButton()
    }

    private fun setUpRecycleView() {
        val delegates: List<DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>> = listOf(
            MessageDelegateAdapter() as DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>,
            DateDelegateAdapter() as DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>,
        )
        adapter = CompositeAdapter(delegates)
        binding.chatRecyclerView.adapter = adapter
        updateList()
    }

    private fun setUpSendButton() {
        binding.messageEditText.doOnTextChanged { text, start, before, count ->
            if (binding.messageEditText.text.toString().isBlank()) {
                binding.sendButton.setImageResource(R.drawable.circle_button_add_message_icon)
                binding.sendButton.setBackgroundResource(R.drawable.circle_button_add_file_bg)
            } else {
                binding.sendButton.setImageResource(R.drawable.circle_button_add_file_icon)
                binding.sendButton.setBackgroundResource(R.drawable.circle_button_add_message_bg)
            }
        }
        binding.sendButton.setOnClickListener {
            addMessage()
        }
    }

    private fun updateList() {
        val userId = userRepository.currentUserId ?: return
        adapter.submitList(
            makeDelegateItems(
                list = messages,
                userId = userId
            )
        )
    }

    private fun addMessage() {
        val messageText = binding.messageEditText.text.toString()
        if (messageText.isBlank()) {
            // TODO: show attachments picker
        } else {
            val message = Message(
                id = id,
                senderName = "", // TODO: implement in the future homeworks
                message = messageText,
                emojiList = emptyList(),
                date = Date(),
                type = MessageView.Model.Type.OUTGOING
            )
            messages.add(message)
            updateList()
            binding.messageEditText.text.clear()
        }
    }

    private fun makeDelegateItems(
        list: List<Message>,
        userId: Int
    ) : MutableList<DelegateItem> {
        return ChatDelegateItemFactory().makeDelegateItems(
            list = list,
            userId = userId,
            showEmojiPicker = { message ->
                showEmojiPicker(
                    message = message
                )
            },
            onSelectEmoji = { message, emojiCode ->
                onSelectEmoji(
                    message = message,
                    emojiCode = emojiCode
                )
            }
        )
    }

    private fun showEmojiPicker(message: Message) {
        val emojiPickerBottomSheetFragment = EmojiPickerBottomSheetFragment(
            emojiList = emojiSetCNCS,
            onSelect = {
                onSelectEmoji(
                    message = message,
                    selectedEmojiCNCS = it
                )
            }
        )
        emojiPickerBottomSheetFragment.show(
            childFragmentManager,
            EmojiPickerBottomSheetFragment.TAG
        )
    }

    private fun onSelectEmoji(
        message: Message,
        selectedEmojiCNCS: EmojiCNCS
    ) {
        val emojiCode = selectedEmojiCNCS.getCodeString()
        onSelectEmoji(
            message = message,
            emojiCode = emojiCode
        )
    }

    private fun onSelectEmoji(
        message: Message,
        emojiCode: String
    ) {
        val userId = userRepository.currentUserId ?: return
        val selectedEmoji = Emoji(
            emojiCode = emojiCode,
            userIds = setOf(userId)
        )
        val newEmojiList = updateEmojiList(
            message = message,
            userId = userId,
            selectedEmoji = selectedEmoji
        )
        val newMessage = message.copy(emojiList = newEmojiList)
        val messageIndex = messages.indexOf(message)
        messages[messageIndex] = newMessage
        updateList()
    }

    private fun updateEmojiList(
        message: Message,
        userId: Int,
        selectedEmoji: Emoji
    ) : List<Emoji> {
        val emojiIndex = message.emojiList.indexOfFirst {
            it.emojiCode == selectedEmoji.emojiCode
        }
        val newEmojiList = if (emojiIndex != -1) {
            val emojiList = message.emojiList.toMutableList()
            val emoji = message.emojiList[emojiIndex]
            val isEmojiClicked = emoji.userIds.contains(userRepository.currentUserId)
            if (isEmojiClicked) {
                emojiList[emojiIndex] = emoji.copy(
                    count = emoji.count - 1,
                    userIds = emoji.userIds.toMutableSet().apply {
                        this.remove(userId)
                    }
                )
            } else {
                emojiList[emojiIndex] = emoji.copy(
                    count = emoji.count + 1,
                    userIds = emoji.userIds.toMutableSet().apply {
                        this.add(userId)
                    }
                )
            }
            if (emojiList[emojiIndex].count == 0) emojiList.removeAt(emojiIndex)
            emojiList
        } else {
            message.emojiList + selectedEmoji
        }.sortedByDescending {
            it.count
        }
        return newEmojiList
    }

    companion object {
        const val TAG = "ChatFragment"
    }
}