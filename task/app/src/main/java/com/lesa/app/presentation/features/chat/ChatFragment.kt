package com.lesa.app.presentation.features.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.ColorUtils
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import com.lesa.app.R
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.databinding.FragmentChatBinding
import com.lesa.app.di.chat.ChatComponentViewModel
import com.lesa.app.domain.model.Stream
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
import com.lesa.app.presentation.features.chat.message.ChangeTopicDialogFragment
import com.lesa.app.presentation.features.chat.message.EditMessageDialogFragment
import com.lesa.app.presentation.features.chat.message.MessageDelegateAdapter
import com.lesa.app.presentation.features.chat.message.MessageView
import com.lesa.app.presentation.features.chat.message_context_menu.MessageContextMenuAction
import com.lesa.app.presentation.features.chat.message_context_menu.MessageContextMenuBottomSheetFragment
import com.lesa.app.presentation.features.chat.message_context_menu.MessageContextMenuBottomSheetFragment.Companion.CONTEXT_MENU_REQUEST_KEY
import com.lesa.app.presentation.features.chat.message_context_menu.MessageContextMenuBottomSheetFragment.Companion.CONTEXT_MENU_RESULT_KEY_ACTION
import com.lesa.app.presentation.features.chat.message_context_menu.MessageContextMenuBottomSheetFragment.Companion.CONTEXT_MENU_RESULT_KEY_MESSAGE_ID
import com.lesa.app.presentation.features.chat.models.EmojiCNCS
import com.lesa.app.presentation.features.chat.models.MessageUi
import com.lesa.app.presentation.navigation.Screens
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
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private var imagePicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {}
    private lateinit var adapter: CompositeAdapter

    @Inject
    lateinit var storeFactory: ChatStoreFactory

    @Inject
    lateinit var router: Router

    override val store: Store<Event, Effect, State> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        val topic : Topic? = requireArguments().getParcelable(TOPIC_KEY)
        val stream : Stream = requireArguments().getParcelable(STREAM_KEY)!!
        storeFactory.create(topic, stream)
    }

    override fun onAttach(context: Context) {
        ViewModelProvider(this).get<ChatComponentViewModel>()
            .component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imagePicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                requireContext().contentResolver
                store.accept(Event.Ui.UploadFile(
                    name = "name",
                    uri = uri
                ))
            }
        }
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
                    bottomInputViews.visibility = View.VISIBLE
                }
                updateList(list = dataToRender.content)
            }
            LceState.Error -> {
                binding.apply {
                    chatRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.VISIBLE
                    shimmerLayout.visibility = View.GONE
                    bottomInputViews.visibility = View.GONE
                }
            }
            LceState.Loading -> {
                binding.apply {
                    chatRecyclerView.visibility = View.GONE
                    error.errorItem.visibility = View.GONE
                    shimmerLayout.visibility = View.VISIBLE
                    bottomInputViews.visibility = View.GONE
                }
            }
            LceState.Idle -> Unit
        }
    }

    override fun handleEffect(effect: Effect) {
        when (effect) {
            is Effect.ShowEmojiPicker -> {
                val emojiPickerBottomSheetFragment = EmojiPickerBottomSheetFragment.newInstance(effect.messageId)
                emojiPickerBottomSheetFragment.show(
                    childFragmentManager, EmojiPickerBottomSheetFragment.TAG
                )
            }

            is Effect.ShowMessageContextMenu -> {
                val messageContextMenuFragment = MessageContextMenuBottomSheetFragment.newInstance(
                    messageId = effect.messageId,
                    isOwn = effect.isOwn
                )
                messageContextMenuFragment.show(
                    childFragmentManager, MessageContextMenuBottomSheetFragment.TAG
                )
            }

            Effect.Back -> {
                activity?.window?.statusBarColor = BLACK
                router.backTo(Screens.StreamsContainer())
            }

            Effect.EmojiError -> {
                val toast = Toast.makeText(context, getText(R.string.error_emoji), Toast.LENGTH_SHORT)
                toast.show()
            }

            Effect.MessageError -> {
                Toast.makeText(
                    context,
                    getText(R.string.error_message_updating),
                    Toast.LENGTH_SHORT
                ).show()
            }

            Effect.ShowAttachmentsPicker -> {
                imagePicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }

            Effect.ClearMessageInput -> {
                binding.messageEditText.text.clear()
            }

            is Effect.UpdateActionButton -> {
                binding.sendButton.setImageResource(effect.icon)
                binding.sendButton.setBackgroundResource(effect.background)
            }

            Effect.MessageDeletingError -> {
                Toast.makeText(
                    context,
                    getText(R.string.error_message_deleting),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ChatEffect.MessageCopied -> {
                val clipboardManager = getSystemService(
                    requireContext(),
                    ClipboardManager::class.java
                ) as ClipboardManager
                val text = effect.text
                val clipData = ClipData.newPlainText("text", text)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(context, getText(R.string.message_copied), Toast.LENGTH_SHORT).show()
            }

            is ChatEffect.EditMessage -> {
                val editMessageDialogFragment = EditMessageDialogFragment.newInstance(
                    messageId = effect.messageId,
                    messageContent = effect.messageContent
                )
                editMessageDialogFragment.show(
                    childFragmentManager, EditMessageDialogFragment.TAG
                )
            }

            is ChatEffect.ShowChangeTopicDialog -> {
                val changeTopicDialogFragment = ChangeTopicDialogFragment.newInstance(
                    messageId = effect.messageId,
                    stream = effect.stream
                )
                changeTopicDialogFragment.show(
                    childFragmentManager, ChangeTopicDialogFragment.TAG
                )
            }

            ChatEffect.MessageChangeTopicError -> {
                Toast.makeText(
                    context,
                    getText(R.string.error_message_changing_topic),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ChatEffect.MessageMovedToAnotherTopic -> {
                Toast.makeText(
                    context,
                    String.format(
                        getString(R.string.message_moved_to_another_topic),
                        effect.topicName
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ChatEffect.OpenTopic -> {
                router.navigateTo(
                    Screens.Chat(
                        topic = effect.topic,
                        stream = effect.stream
                    )
                )
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
        setUpMessageContextMenu()
        setUpEditMessageDialog()
        setUpChangeTopicDialog()
        setUpTopicInput()
    }

    private fun setUpTitle() {
        val topic = store.states.value.topic
        val stream = store.states.value.stream
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
        val color = ColorUtils.blendARGB(
            Color.parseColor(stream.color),
            if (isDarkTheme) BLACK else WHITE,
            COLOR_RATIO
        )
        binding.toolBar.setBackgroundColor(color)
        activity?.window?.statusBarColor = color
        binding.streamName.text = stream.name
        if (topic != null) {
            binding.topicName.text = String.format(
                requireContext().getString(R.string.title_chat_topic_name), topic.name
            )
            binding.topicName.visibility = View.VISIBLE
        } else {
            binding.topicName.visibility = View.GONE
        }
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
                            store.accept(Event.Ui.ShowContextMessageBottomSheet(id))
                        },
                        onEmojiClick = { id, emojiCode ->
                            onSelectEmoji(
                                messageId = id,
                                emojiCode = emojiCode
                            )
                        },
                        onPlusButtonClick = { id ->
                            store.accept(Event.Ui.ShowEmojiPicker(messageId = id))
                        },
                        onTopicClick = {
                            store.accept(Event.Ui.OnTopicClick(topicName = it))
                        }
                    )
                ),
                DateDelegateAdapter()
            )
        )
        binding.chatRecyclerView.adapter = adapter

        binding.chatRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = binding.chatRecyclerView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstVisibleItemPosition()
                    if (position < LOAD_TRIGGER_MESSAGE_COUNT && dy < 0) store.accept(Event.Ui.FetchMoreMessages)
                }
            }
        )
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

    private fun setUpMessageContextMenu() {
        childFragmentManager.setFragmentResultListener(
            CONTEXT_MENU_REQUEST_KEY, this
        ) { _, bundle ->
            val actionName = bundle.getString(CONTEXT_MENU_RESULT_KEY_ACTION)
            val action = actionName?.let { MessageContextMenuAction.valueOf(it) }
            val messageId = bundle.getInt(CONTEXT_MENU_RESULT_KEY_MESSAGE_ID)
            if (action == null || messageId == 0) return@setFragmentResultListener
            store.accept(
                Event.Ui.SelectMenuAction(
                    action = action,
                    messageId = messageId
                )
            )
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

    private var shouldScrollToBottom = true

    private fun updateList(list: List<MessageUi>) {
        val delegateItems = makeDelegateItems(list = list)
        adapter.submitList(delegateItems) {
            if (shouldScrollToBottom) binding.chatRecyclerView.layoutManager?.scrollToPosition(delegateItems.size - 1)
            shouldScrollToBottom = false
        }
    }

    private fun onActionButtonClicked() {
        val messageText = binding.messageEditText.text.toString()
        val topicName = binding.topicInput.text.toString()
        store.accept(
            ChatEvent.Ui.ActionButtonClicked(
                content = messageText,
                topicName = topicName
            )
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

    private fun setUpEditMessageDialog() {
        childFragmentManager.setFragmentResultListener(
            EditMessageDialogFragment.EDIT_MESSAGE_REQUEST_KEY,
            this
        ) { key, bundle ->
            val id = bundle.getInt(EditMessageDialogFragment.EDIT_MESSAGE_RESULT_KEY_ID)
            val content = bundle.getString(EditMessageDialogFragment.EDIT_MESSAGE_RESULT_KEY_CONTENT)
            content?.let {
                store.accept(
                    ChatEvent.Ui.EditMessage(
                        messageId = id,
                        messageContent = content
                    )
                )
            }
        }
    }

    private fun setUpChangeTopicDialog() {
        childFragmentManager.setFragmentResultListener(
            ChangeTopicDialogFragment.CHANGE_TOPIC_REQUEST_KEY,
            this
        ) { key, bundle ->
            val topicName = bundle.getString(ChangeTopicDialogFragment.CHANGE_TOPIC_TOPIC_NAME_RESULT_KEY)
            val messageId = bundle.getInt(ChangeTopicDialogFragment.CHANGE_TOPIC_MESSAGE_ID_RESULT_KEY)
            topicName?.let {
                store.accept(
                    ChatEvent.Ui.ChangeMessageTopic(
                        messageId = messageId,
                        topicName = it
                    )
                )
            }
        }
    }

    private fun setUpTopicInput() {
        if (store.states.value.topic != null) {
            binding.topicInput.visibility = View.GONE
            binding.senToTopicTextView.visibility = View.GONE
        } else {
            binding.topicInput.visibility = View.VISIBLE
            val list = store.states.value.stream.topics.map { it.name }
            val placeHolderAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                list
            )
            binding.topicInput.setAdapter(placeHolderAdapter)
        }
    }

    companion object {
        private const val TOPIC_KEY = "topic_key"
        private const val STREAM_KEY = "stream_key"
        private const val LOAD_TRIGGER_MESSAGE_COUNT = 5
        private const val COLOR_RATIO = 0.6f

        fun createArguments(topic: Topic?, stream: Stream) = bundleOf(
            TOPIC_KEY to topic,
            STREAM_KEY to stream
        )

        fun getNewInstance(topic: Topic?, stream: Stream): ChatFragment {
            return ChatFragment().apply {
                arguments = createArguments(topic, stream)
            }
        }
    }
}