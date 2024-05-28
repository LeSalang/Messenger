package com.lesa.app.presentation.features.chat.date

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.R
import com.lesa.app.composite_adapter.DelegateAdapter
import com.lesa.app.databinding.ItemDateBinding
import java.text.SimpleDateFormat
import java.util.Date

class DateDelegateAdapter :
    DelegateAdapter<DateDelegateItem, DateDelegateAdapter.ViewHolder>(DateDelegateItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: DateDelegateItem) {
        holder.bind(item.date)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemDateBinding.bind(itemView)
        private val formatter = SimpleDateFormat(DATE_PATTERN)

        fun bind(model: Date) {
            binding.date.text = formatter.format(model)
        }
    }
}

const val DATE_PATTERN = "dd MMM"