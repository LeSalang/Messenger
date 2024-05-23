package com.lesa.app.presentation.features.chat.message

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.composite_adapter.CompositeAdapter
import com.lesa.app.composite_adapter.delegatesList
import com.lesa.app.domain.model.Stream
import com.lesa.app.presentation.features.streams.TopicDelegateAdapter
import com.lesa.app.presentation.features.streams.TopicDelegateItem

class ChangeTopicDialogFragment : DialogFragment() {
    private lateinit var adapter: CompositeAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.Dialog)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_change_topic, null)
            builder.setView(view)

            val dialog = builder.create()
            val messageId = arguments?.getInt(CHANGE_TOPIC_MESSAGE_ID_KEY)
            val stream: Stream? = arguments?.getParcelable(CHANGE_TOPIC_STREAM_KEY)
            val topicList = stream?.topics?.map { topic ->
                TopicDelegateItem(topic)
            }
            adapter = CompositeAdapter(
                delegatesList(
                    TopicDelegateAdapter(
                        onClick = { topic ->
                            val topicName = topic.name
                            val bundle = bundleOf(
                                CHANGE_TOPIC_MESSAGE_ID_RESULT_KEY to messageId,
                                CHANGE_TOPIC_TOPIC_NAME_RESULT_KEY to topicName
                            )
                            parentFragmentManager.setFragmentResult(
                                CHANGE_TOPIC_REQUEST_KEY,
                                bundle
                            )
                            dismiss()
                        }
                    )
                )
            )
            adapter.submitList(topicList)

            val rcView = view.findViewById<RecyclerView>(R.id.changeTopicRecycleView)
            rcView.adapter = adapter
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "ChangeTopicDialogFragment tag"
        const val CHANGE_TOPIC_REQUEST_KEY = "ChangeTopicDialogFragment request key"
        const val CHANGE_TOPIC_MESSAGE_ID_KEY = "ChangeTopicDialogFragment message ID key"
        const val CHANGE_TOPIC_MESSAGE_ID_RESULT_KEY = "ChangeTopicDialogFragment message ID result key"
        const val CHANGE_TOPIC_TOPIC_NAME_LIST_KEY = "ChangeTopicDialogFragment topic name key"
        const val CHANGE_TOPIC_TOPIC_NAME_RESULT_KEY = "ChangeTopicDialogFragment topic name result key"
        const val CHANGE_TOPIC_STREAM_KEY = "ChangeTopicDialogFragment stream key"

        fun newInstance(messageId: Int, stream: Stream): ChangeTopicDialogFragment {
            return ChangeTopicDialogFragment().apply {
                arguments = bundleOf(
                    CHANGE_TOPIC_MESSAGE_ID_KEY to messageId,
                    CHANGE_TOPIC_STREAM_KEY to stream
                )
            }
        }
    }
}