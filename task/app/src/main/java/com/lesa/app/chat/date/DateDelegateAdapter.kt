package com.lesa.app.chat.date

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.compositeAdapter.DelegateAdapter
import com.lesa.app.databinding.DateItemBinding
import java.text.SimpleDateFormat
import java.util.Date

class DateDelegateAdapter:
    DelegateAdapter<DateDelegateItem, DateDelegateAdapter.ViewHolder> (DateDelegateItem::class.java){

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.date_item, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, item: DateDelegateItem) {
            holder.bind(item.date)
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = DateItemBinding.bind(itemView)

        fun bind(model: Date) {
            val formatter = SimpleDateFormat("dd MMM")
            binding.date.text = formatter.format(model)
        }
    }
}