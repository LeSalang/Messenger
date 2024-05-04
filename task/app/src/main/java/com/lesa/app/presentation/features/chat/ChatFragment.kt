package com.lesa.app.presentation.features.chat

import android.content.Context
import android.graphics.Color
import android.graphics.Color.BLACK
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.graphics.ColorUtils
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import com.lesa.app.R
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.databinding.FragmentChatBinding
import com.lesa.app.di.chat.ChatComponentViewModel
import com.lesa.app.domain.model.Topic
import com.lesa.app.presentation.elm.ElmBaseFragment
import com.lesa.app.presentation.features.chat.date.DateDelegateAdapter
import com.lesa.app.presentation.features.chat.elm.ChatEffect
import com.lesa.app.presentation.features.chat.elm.ChatEvent
import com.lesa.app.presentation.features.chat.elm.ChatState
import com.lesa.app.presentation.features.chat.elm.ChatStoreFactory
import com.lesa.app.presentation.features.chat.emoji_picker.EmojiPickerBottomSheetFragment
import com.lesa.app.presentation.features.chat.emoji_picker.EmojiPickerBottomSheetFragment.Companion.ON_SELECT_EMOJI_REQUEST_KEY
import com.lesa.app.presentation.features.chat.emoji_picker.EmojiPickerBottomSheetFragment.Companion.SELECTED_EMOJI_KEY
import com.lesa.app.presentation.features.chat.emoji_picker.EmojiPickerBottomSheetFragment.Companion.SELECTED_MESSAGE_KEY
import com.lesa.app.presentation.features.chat.message.MessageDelegateAdapter
import com.lesa.app.presentation.features.chat.message.MessageView
import com.lesa.app.presentation.features.chat.models.EmojiCNCS
import com.lesa.app.presentation.features.chat.models.MessageUi
import com.lesa.app.presentation.utils.BottomBarViewModel
import com.lesa.app.presentation.utils.LceState
import kotlinx.coroutines.launch
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import com.lesa.app.presentation.features.chat.elm.ChatEffect as Effect
import com.lesa.app.presentation.features.chat.elm.ChatEvent as Event
import com.lesa.app.presentation.features.chat.elm.ChatState as State

class ChatFragment : ElmBaseFragment<Effect, State, Event>(
    R.layout.fragment_chat
) {
    private val binding: FragmentChatBinding by viewBinding()
    private lateinit var adapter: CompositeAdapter
    private lateinit var bottomBarViewModel: BottomBarViewModel

    @Inject
    lateinit var storeFactory: ChatStoreFactory

    @Inject
    lateinit var router: Router

    override val store: Store<Event, Effect, State> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        val topic : Topic = requireArguments().getParcelable(TOPIC_KEY)!! // TODO
        storeFactory.create(topic)
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChatComponentViewModel>()
            .component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomBarViewModel = ViewModelProvider(requireActivity())[BottomBarViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)
        store.accept(ChatEvent.Ui.Init)
        setUpViews()
    }

    override fun onStart() {
        viewLifecycleOwner.lifecycleScope.launch {
            bottomBarViewModel.isBottomBarShown.emit(false)
        }
        super.onStart()
    }

    override fun onStop() {
        viewLifecycleOwner.lifecycleScope.launch {
            bottomBarViewModel.isBottomBarShown.emit(true)
        }
        super.onStop()
    }

    override fun render(state: ChatState) {
        when (val dataToRender = state.lceState) {
            is LceState.Content -> {
                binding.apply {
                    chatRecyclerView.visibility = View.VISIBLE
                    error.errorItem.visibility = View.GONE
                    shimmerLayout.visibility = View.GONE
                    messageEditText.visibility = View.VISIBLE
                    sendButton.visibility = View.VISIBLE
                }
                updateList(list = dataToRender.content)
            }
            LceState.Error -> {
                binding.apply {
                    chatRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.VISIBLE
                    shimmerLayout.visibility = View.GONE
                    messageEditText.visibility = View.GONE
                    sendButton.visibility = View.GONE
                }
            }
            LceState.Loading -> {
                binding.apply {
                    chatRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.GONE
                    shimmerLayout.visibility = View.VISIBLE
                    messageEditText.visibility = View.VISIBLE
                    sendButton.visibility = View.VISIBLE
                }
            }

            LceState.Idle -> TODO()
        }
    }

    override fun handleEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowEmojiPicker -> {
                val emojiPickerBottomSheetFragment = EmojiPickerBottomSheetFragment.newInstance(effect.emojiId)
                emojiPickerBottomSheetFragment.show(
                    childFragmentManager, EmojiPickerBottomSheetFragment.TAG
                )
            }

            Effect.Back -> {
                activity?.window?.statusBarColor = BLACK
                router.exit()
            }

            Effect.EmojiError -> {
                val toast = Toast.makeText(context, getText(R.string.error_emoji), Toast.LENGTH_SHORT)
                toast.show()
            }

            Effect.ShowAttachmentsPicker -> {
                // TODO: show attachments picker
            }

            Effect.ClearMessageInput -> {
                binding.messageEditText.text.clear()
            }

            is ChatEffect.UpdateActionButton -> {
                binding.sendButton.setImageResource(effect.icon)
                binding.sendButton.setBackgroundResource(effect.background)
            }
        }
    }

    private fun setUpViews() {
        setUpTitle()
        setUpBackButton()
        setUpRecycleView()
        setUpEmojiPicker()
        setupRefreshButton()
        setUpActions()
    }

    private fun setUpTitle() {
        val topic = store.states.value.topic
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
            store.accept(Event.Ui.Back)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            store.accept(Event.Ui.Back)
        }
    }

    private fun setUpRecycleView() {
        adapter = CompositeAdapter(
            delegatesList(
                MessageDelegateAdapter(
                    actions = MessageView.Actions(
                        onLongClick = { id ->
                            store.accept(Event.Ui.ShowEmojiPicker(emojiId = id))
                        },
                        onEmojiClick = { id, emojiCode ->
                            onSelectEmoji(
                                messageId = id,
                                emojiCode = emojiCode
                            )
                        },
                        onPlusButtonClick = { id ->
                            store.accept(Event.Ui.ShowEmojiPicker(emojiId = id))
                        }
                    )
                ),
                DateDelegateAdapter()
            )
        )
        binding.chatRecyclerView.adapter = adapter
    }

    private fun setUpEmojiPicker() {
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

    private fun setupRefreshButton() {
        binding.error.refreshButton.setOnClickListener {
            store.accept(Event.Ui.Init)
        }
    }

    private fun setUpActions() {
        binding.messageEditText.doOnTextChanged { _, _, _, _ ->
            val text = binding.messageEditText.text.toString()
            store.accept(Event.Ui.MessageTextChanged(text))
        }
        binding.messageEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                onActionButtonClicked()
            }
            return@setOnEditorActionListener true
        }
        binding.sendButton.setOnClickListener {
            onActionButtonClicked()
        }
    }

    private fun updateList(list: List<MessageUi>) {
        val delegateItems = makeDelegateItems(list = list)
        adapter.submitList(delegateItems) {
            binding.chatRecyclerView.layoutManager?.scrollToPosition(delegateItems.size - 1)
        }
    }

    private fun onActionButtonClicked() {
        val messageText = binding.messageEditText.text.toString()
        store.accept(
            ChatEvent.Ui.ActionButtonClicked(content = messageText)
        )
    }

    private fun makeDelegateItems(
        list: List<MessageUi>,
    ): MutableList<DelegateItem> {
        return ChatDelegateItemFactory().makeDelegateItems(
            list = list
        )
    }

    private fun onSelectEmoji(
        messageId: Int,
        selectedEmojiCNCS: EmojiCNCS,
    ) {
        val emojiCode = selectedEmojiCNCS.code
        onSelectEmoji(
            messageId = messageId,
            emojiCode = emojiCode
        )
    }

    private fun onSelectEmoji(
        messageId: Int,
        emojiCode: String,
    ) {
        store.accept(
            Event.Ui.SelectEmoji(
                messageId = messageId,
                emojiCode = emojiCode
            )
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