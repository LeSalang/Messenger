package com.lesa.app.chat

import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.model.Message

class MessageListAdapter(
    private val messages: List<Message>
) : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.messagasampla, parent, false
        )
        //val view = MessageView(parent.context, attachToRoot = false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(model: Message) {
            with(view) {
                /*update(MessageView.Model(
                    R.drawable.cat_1,
                    resources.getString(R.string.sample_name),
                    resources.getString(R.string.sample_message)
                ))*/
            }
        }


    }
}