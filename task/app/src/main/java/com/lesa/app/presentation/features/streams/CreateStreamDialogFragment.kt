package com.lesa.app.presentation.features.streams

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.lesa.app.R

class CreateStreamDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.Dialog)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_create_stream, null)
            builder.setView(view)
            val dialog = builder.create()

            val editText = view.findViewById<EditText>(R.id.newTopicName)

            val button = view.findViewById<Button>(R.id.positiveButton)
            button.setOnClickListener {
                val text = editText.text.toString()
                setFragmentResult(requestKey = RESULT_KEY, result = bundleOf(BUNDLE_KEY to text))
                dismiss()
            }
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")

    }
    companion object {
        const val TAG = "CreateStreamDialog"
        const val RESULT_KEY = "CreateStreamDialog result key"
        const val BUNDLE_KEY = "CreateStreamDialog bundle key"
    }
}