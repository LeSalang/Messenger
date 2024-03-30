package com.lesa.app.emoji_picker

import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lesa.app.databinding.FragmentEmojiPickerBottomSheetBinding
import com.lesa.app.model.EmojiCNCS

class EmojiPickerBottomSheetFragment(
    private val emojiList: List<EmojiCNCS>,
    private val onSelect: (EmojiCNCS) -> Unit,
) : BottomSheetDialogFragment() {
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
        val emojiPicker = binding.emojiPickerRecyclerView
        val flexboxLayoutManager = FlexboxLayoutManager(context)

        adapter = EmojiPickerAdapter(emojiList)
        emojiPicker.adapter = adapter
        emojiPicker.layoutManager = flexboxLayoutManager

        val screenSize = requireActivity().resources.displayMetrics.heightPixels

        val behavior = (this.dialog as BottomSheetDialog).behavior
        behavior.maxHeight = screenSize - 200
        behavior.peekHeight = screenSize / 2

        val itemTouchListener =
            RecyclerTouchListener(requireContext(), object : RecyclerTouchListener.ClickListener {
                override fun onClick(view: View, position: Int) {
                    val emoji = emojiList[position]
                    onSelect.invoke(emoji)
                    dismiss()
                }
            })
        emojiPicker.addOnItemTouchListener(itemTouchListener)
    }

    class RecyclerTouchListener(
        context: Context,
        private val clickListener: ClickListener,
    ) : RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean = true
            })

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child: View? = rv.findChildViewUnder(e.x, e.y)
            if (child != null && gestureDetector.onTouchEvent(e)) clickListener.onClick(
                child,
                rv.getChildAdapterPosition(child)
            )
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        interface ClickListener {
            fun onClick(view: View, position: Int)
        }
    }

    companion object {
        const val TAG = "EmojiPickerBottomSheet"
    }
}