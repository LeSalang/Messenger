package com.lesa.app.presenation.chat.emoji_picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lesa.app.databinding.FragmentEmojiPickerBottomSheetBinding
import com.lesa.app.domain.model.EmojiCNCS
import com.lesa.app.domain.model.emojiSetCNCS

class EmojiPickerBottomSheetFragment : BottomSheetDialogFragment() {
    private val binding: FragmentEmojiPickerBottomSheetBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private lateinit var adapter: EmojiPickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()

        val screenSize = requireActivity().resources.displayMetrics.heightPixels

        val behavior = (this.dialog as BottomSheetDialog).behavior
        behavior.maxHeight = screenSize - 200
        behavior.peekHeight = screenSize / 2
    }

    private fun setUpRecycleView() {
        adapter = EmojiPickerAdapter(emojiList = emojiSetCNCS, onSelect = {
            selectEmoji(it)
        })
        val emojiPicker = binding.emojiPickerRecyclerView
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        emojiPicker.adapter = adapter
        emojiPicker.layoutManager = flexboxLayoutManager
    }

    private fun selectEmoji(emoji: EmojiCNCS) {
        val bundle = Bundle()
        bundle.putParcelable(SELECTED_EMOJI_KEY, emoji)
        arguments?.getInt(SELECTED_MESSAGE_KEY)?.let { bundle.putInt(SELECTED_MESSAGE_KEY, it) }
        parentFragmentManager.setFragmentResult(
            ON_SELECT_EMOJI_REQUEST_KEY, bundle
        )
        dismiss()
    }

    companion object {
        const val TAG = "EmojiPickerBottomSheet"
        const val ON_SELECT_EMOJI_REQUEST_KEY = "On select emoji request key"
        const val SELECTED_EMOJI_KEY = "Selected emoji key"
        const val SELECTED_MESSAGE_KEY = "Selected message key"

        fun newInstance(messageId: Int): EmojiPickerBottomSheetFragment {
            return EmojiPickerBottomSheetFragment().apply {
                arguments = bundleOf(SELECTED_MESSAGE_KEY to messageId)
            }
        }
    }
}