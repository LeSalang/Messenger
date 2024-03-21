package com.lesa.app.emojiPicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lesa.app.allEmojis
import com.lesa.app.databinding.FragmentEmojiPickerBottomSheetBinding

class EmojiPickerBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentEmojiPickerBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EmojiPickerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEmojiPickerBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EmojiPickerAdapter(allEmojis)
        binding.emojiPickerRecyclerView.adapter = adapter

        val flexboxLayoutManager = FlexboxLayoutManager(context)
        binding.emojiPickerRecyclerView.layoutManager = flexboxLayoutManager
    }

    companion object {
        const val TAG = "EmojiPickerBottomSheet"
    }
}