package com.lesa.app.presentation.features.chat.message_context_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lesa.app.databinding.FragmentContextMenuBottomSheetBinding

class MessageContextMenuBottomSheetFragment : BottomSheetDialogFragment() {
    private val binding: FragmentContextMenuBottomSheetBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private lateinit var adapter: ContextMenuAdapter<MessageContextMenuAction>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val isOwn = arguments?.getBoolean(CONTEXT_MENU_KEY_MESSAGE_IS_OWN) ?: false
        val list = if (isOwn) {
            MessageContextMenuAction.entries
        } else {
            MessageContextMenuAction.entries.filter {
                it.isAvailableForIncomingMessage
            }
        }

        adapter = ContextMenuAdapter(
            menuActionList = list,
            onMenuItemClick = { action ->
                val bundle = bundleOf(
                    CONTEXT_MENU_RESULT_KEY_ACTION to action.name,
                    CONTEXT_MENU_RESULT_KEY_MESSAGE_ID to arguments?.getInt(
                        CONTEXT_MENU_KEY_MESSAGE_ID
                    )
                )
                parentFragmentManager.setFragmentResult(
                    CONTEXT_MENU_REQUEST_KEY, bundle
                )
                dismiss()
            }
        )
        val bottomSheet = binding.messageContextMenuRecyclerView
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        bottomSheet.adapter = adapter
        bottomSheet.layoutManager = flexboxLayoutManager
    }

    companion object {
        const val TAG = "ContextMenuBottomSheetFragment"
        const val CONTEXT_MENU_REQUEST_KEY = "Context menu request key"
        const val CONTEXT_MENU_RESULT_KEY_ACTION = "Result key action"
        const val CONTEXT_MENU_RESULT_KEY_MESSAGE_ID = "Result key message ID"
        const val CONTEXT_MENU_KEY_MESSAGE_ID = "Context menu key message ID"
        const val CONTEXT_MENU_KEY_MESSAGE_IS_OWN = "Context menu key message is own"

        fun newInstance(messageId: Int, isOwn: Boolean): MessageContextMenuBottomSheetFragment {
            return MessageContextMenuBottomSheetFragment().apply {
                arguments = bundleOf(
                    CONTEXT_MENU_KEY_MESSAGE_ID to messageId,
                    CONTEXT_MENU_KEY_MESSAGE_IS_OWN to isOwn
                )
            }
        }
    }
}