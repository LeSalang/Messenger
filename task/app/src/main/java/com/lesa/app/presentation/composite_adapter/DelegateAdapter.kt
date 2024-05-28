package com.lesa.app.presentation.composite_adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class DelegateAdapter<Item : DelegateItem, in ViewHolder : RecyclerView.ViewHolder>(
    val itemClass: Class<out Item>,
) {
    abstract fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun onBindViewHolder(holder: ViewHolder, item: Item)
}
