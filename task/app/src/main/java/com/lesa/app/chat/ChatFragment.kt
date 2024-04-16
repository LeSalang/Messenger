package com.lesa.app.chat

import android.graphics.Color
import android.graphics.Color.BLACK
import android.os.Bundle
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
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
import com.lesa.app.model.EmojiCNCS
import com.lesa.app.model.Message
import com.lesa.app.model.Topic
import kotlinx.coroutines.launch

class ChatFragment : Fragment(R.layout.fragment_chat) {
    private val binding: FragmentChatBinding by viewBinding()
    private lateinit var adapter: CompositeAdapter

    private val viewModel: ChatViewModel by viewModels() {
        ChatViewModelFactory(
            context = requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        val topic: Topic? = requireArguments().getParcelable(TOPIC_KEY)
        viewModel.topic = topic
        topic?.let { setUpTitle(it) }

        lifecycleScope.launch {
            viewModel.state.collect(::render)
        }

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
        viewModel.loadMessages()
    }

    private fun setUpViews() {
        setUpRecycleView()
        setUpSendButton()
        setUpBackButton()
    }

    private fun render(state: ChatScreenState) {
        when (state) {
            is ChatScreenState.DataLoaded -> {
                binding.apply {
                    chatRecyclerView.visibility = View.VISIBLE
                    error.errorItem.visibility = View.GONE
                    shimmerLayout.visibility = View.GONE
                }
            }
            ChatScreenState.Error -> {
                binding.apply {
                    chatRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.VISIBLE
                    shimmerLayout.visibility = View.GONE
                    messageEditText.visibility = View.GONE
                    sendButton.visibility = View.GONE
                }
            }
            ChatScreenState.Loading -> {
                binding.apply {
                    chatRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.GONE
                    shimmerLayout.visibility = View.VISIBLE
                }
            }
        }
        updateList()
    }

    private fun setUpTitle(topic: Topic) {
        val color = ColorUtils.blendARGB(Color.parseColor(topic.color), BLACK, 0.6f)
        binding.toolBar.setBackgroundColor(color)
        activity?.window?.statusBarColor = color
        binding.streamName.text = topic.streamName
        binding.topicName.text = String.format(
            requireContext().getString(R.string.title_chat_Topic_name),
            topic.name
        )
    }

    private fun setUpBackButton() {
        binding.backButton.setOnClickListener {
            activity?.window?.statusBarColor = BLACK
            App.INSTANCE.router.navigateTo(Screens.Main()) // TODO: 'exit()' doesn't work :c
        }
    }

    private fun setUpRecycleView() {
        adapter = CompositeAdapter(
            delegatesList(
                MessageDelegateAdapter(
                    actions = MessageView.Actions(
                        onLongClick = { id ->
                            showEmojiPicker(
                                message = viewModel.state.value.messages.first {
                                    it.id == id
                                }
                            )
                        },
                        onEmojiClick = { id, emojiCode ->
                            onSelectEmoji(
                                message = viewModel.state.value.messages.first {
                                    it.id == id
                                },
                                emojiCode = emojiCode
                            )
                        },
                        onPlusButtonClick = { id ->
                            showEmojiPicker(
                                message = viewModel.state.value.messages.first {
                                    it.id == id
                                }
                            )
                        }
                    )
                ),
                DateDelegateAdapter()
            )
        )
        binding.chatRecyclerView.adapter = adapter
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

    private fun updateList() {
        val delegateItems = makeDelegateItems(list = viewModel.state.value.messages)
        adapter.submitList(delegateItems) {
            binding.chatRecyclerView.layoutManager?.scrollToPosition(delegateItems.size - 1)
        }
    }

    private fun addMessage() {
        val messageText = binding.messageEditText.text.toString()
        if (messageText.isBlank()) {
            // TODO: show attachments picker
        } else {
            viewModel.sendMessages(
                content = messageText,
            )
            updateList()
            binding.messageEditText.text.clear()
        }
    }

    private fun makeDelegateItems(
        list: List<Message>,
    ): MutableList<DelegateItem> {
        return ChatDelegateItemFactory().makeDelegateItems(
            list = list
        )
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
        val message: Message = viewModel
            .state
            .value
            .messages
            .firstOrNull {
                it.id == messageId
            } ?: return
        val emojiCode = selectedEmojiCNCS.code
        onSelectEmoji(
            message = message,
            emojiCode = emojiCode
        )
    }

    private fun onSelectEmoji(
        message: Message,
        emojiCode: String,
    ) {
        viewModel.onSelectEmoji(
            message = message,
            emojiCode = emojiCode
        )
    }

    companion object {
        private const val TOPIC_KEY = "topic_key"
        fun getNewInstance(topic: Topic): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(TOPIC_KEY, topic)
                }
            }
        }
    }
}