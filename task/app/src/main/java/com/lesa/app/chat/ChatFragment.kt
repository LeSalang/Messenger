package com.lesa.app.chat

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.lesa.app.App
import com.lesa.app.R
import com.lesa.app.Screens
import com.lesa.app.chat.date.DateDelegateAdapter
import com.lesa.app.chat.emoji_picker.EmojiPickerBottomSheetFragment
import com.lesa.app.chat.emoji_picker.EmojiPickerBottomSheetFragment.Companion.ON_SELECT_EMOJI_REQUEST_KEY
import com.lesa.app.chat.emoji_picker.EmojiPickerBottomSheetFragment.Companion.SELECTED_EMOJI_KEY
import com.lesa.app.chat.emoji_picker.EmojiPickerBottomSheetFragment.Companion.SELECTED_MESSAGE_KEY
import com.lesa.app.chat.message.MessageDelegateAdapter
import com.lesa.app.chat.message.MessageView
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.databinding.FragmentChatBinding
import com.lesa.app.model.Emoji
import com.lesa.app.model.EmojiCNCS
import com.lesa.app.model.Message
import com.lesa.app.model.Topic
import com.lesa.app.repositories.UserRepository
import com.lesa.app.stubChannels
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpTitle(requireArguments().getInt(CHANNEL_ID_KEY))

        childFragmentManager.setFragmentResultListener(
            ON_SELECT_EMOJI_REQUEST_KEY, this
        ) { requestKey, bundle ->
            val emoji: EmojiCNCS? = bundle.getParcelable(SELECTED_EMOJI_KEY)
            val messageId = bundle.getInt(SELECTED_MESSAGE_KEY)
            if (emoji != null) {
                onSelectEmoji(
                    messageId = messageId, selectedEmojiCNCS = emoji
                )
            }
        }
    }

    private fun setUpViews() {
        setUpRecycleView()
        setUpSendButton()
        setUpBackButton()
    }

    private fun setUpRecycleView() {
        adapter = CompositeAdapter(
            delegatesList(
                MessageDelegateAdapter(), DateDelegateAdapter()
            )
        )
        binding.chatRecyclerView.adapter = adapter
        updateList()
    }

    private fun setUpSendButton() {
        binding.messageEditText.doOnTextChanged { _, _, _, _ ->
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

    private fun setUpBackButton() {
        binding.backButton.setOnClickListener {
            App.INSTANCE.router.backTo(Screens.Channels())
            activity?.window?.statusBarColor = resources.getColor(R.color.gray_18)
        }
    }

    private fun updateList() {
        val userId = userRepository.currentUserId ?: return
        adapter.submitList(
            makeDelegateItems(
                list = messages, userId = userId
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
        userId: Int,
    ): MutableList<DelegateItem> {
        return ChatDelegateItemFactory().makeDelegateItems(list = list,
            userId = userId,
            showEmojiPicker = { message ->
                showEmojiPicker(
                    message = message
                )
            },
            onSelectEmoji = { message, emojiCode ->
                onSelectEmoji(
                    message = message, emojiCode = emojiCode
                )
            })
    }

    private fun showEmojiPicker(message: Message) {
        val emojiPickerBottomSheetFragment = EmojiPickerBottomSheetFragment.newInstance(message.id)
        emojiPickerBottomSheetFragment.show(
            childFragmentManager, EmojiPickerBottomSheetFragment.TAG
        )
    }

    private fun onSelectEmoji(
        messageId: Int,
        selectedEmojiCNCS: EmojiCNCS,
    ) {
        val message: Message = messages.firstOrNull {
            it.id == messageId
        } ?: return
        val emojiCode = selectedEmojiCNCS.getCodeString()
        onSelectEmoji(
            message = message, emojiCode = emojiCode
        )
    }

    private fun onSelectEmoji(
        message: Message,
        emojiCode: String,
    ) {
        val userId = userRepository.currentUserId ?: return
        val selectedEmoji = Emoji(
            emojiCode = emojiCode, userIds = setOf(userId)
        )
        val newEmojiList = updateEmojiList(
            message = message, userId = userId, selectedEmoji = selectedEmoji
        )
        val newMessage = message.copy(emojiList = newEmojiList)
        val messageIndex = messages.indexOf(message)
        messages[messageIndex] = newMessage
        updateList()
    }

    private fun updateEmojiList(
        message: Message,
        userId: Int,
        selectedEmoji: Emoji,
    ): List<Emoji> {
        val emojiIndex = message.emojiList.indexOfFirst {
            it.emojiCode == selectedEmoji.emojiCode
        }
        val newEmojiList = if (emojiIndex != -1) {
            val emojiList = message.emojiList.toMutableList()
            val emoji = message.emojiList[emojiIndex]
            val isEmojiClicked = emoji.userIds.contains(userRepository.currentUserId)
            if (isEmojiClicked) {
                emojiList[emojiIndex] = emoji.copy(count = emoji.count - 1,
                    userIds = emoji.userIds.toMutableSet().apply {
                        this.remove(userId)
                    })
            } else {
                emojiList[emojiIndex] = emoji.copy(count = emoji.count + 1,
                    userIds = emoji.userIds.toMutableSet().apply {
                        this.add(userId)
                    })
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

    private fun setUpTitle(id: Int) {
        val topics = mutableListOf<Topic>()
        stubChannels.map { channel ->
            channel.topics.forEach {
                topics.add(it)
            }
        }
        val topic = topics.first {
            it.id == id
        }
        binding.toolBar.setBackgroundResource(topic.color)
        activity?.window?.statusBarColor = resources.getColor(topic.color)
        binding.title.text = topic.name
    }

    companion object {
        private const val CHANNEL_ID_KEY = "user_id_key"

        fun getNewInstance(channelId: Int): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putInt(CHANNEL_ID_KEY, channelId)
                }
            }
        }
    }
}