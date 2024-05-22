package com.lesa.app.presentation.features.chat.message_context_menu

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.lesa.app.R

class EditMessageDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.Dialog)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_edit_message, null)
            builder.setView(view)
            val dialog = builder.create()
            val text = arguments?.getString(EDIT_MESSAGE_CONTENT_KEY)
            val id = arguments?.getInt(EDIT_MESSAGE_ID_KEY)

            val editText = view.findViewById<EditText>(R.id.newMessageText)
            editText.setText(text)

            val button = view.findViewById<Button>(R.id.editMessagePositiveButton)
            button.setOnClickListener {
                val message = editText.text.toString()
                parentFragmentManager.setFragmentResult(
                    EDIT_MESSAGE_REQUEST_KEY,
                    bundleOf(
                        EDIT_MESSAGE_RESULT_KEY_CONTENT to message,
                        EDIT_MESSAGE_RESULT_KEY_ID to id
                    )
                )
                dismiss()
            }
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "EditMessageDialog"
        const val EDIT_MESSAGE_REQUEST_KEY = "EditMessageDialog request key"
        const val EDIT_MESSAGE_CONTENT_KEY = "EditMessageDialog content key"
        const val EDIT_MESSAGE_ID_KEY = "EditMessageDialog id key"
        const val EDIT_MESSAGE_RESULT_KEY_CONTENT = "EditMessageDialog result key for message content"
        const val EDIT_MESSAGE_RESULT_KEY_ID = "EditMessageDialog result key for message id"

        fun newInstance(messageId: Int, messageContent: String): EditMessageDialogFragment {
            return EditMessageDialogFragment().apply {
                arguments = bundleOf(
                    EDIT_MESSAGE_ID_KEY to messageId,
                    EDIT_MESSAGE_CONTENT_KEY to messageContent
                )
            }
        }
    }
}