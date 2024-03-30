package com.lesa.app.compositeAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CompositeAdapter(
    private val delegates: List<DelegateAdapter<DelegateItem, RecyclerView.ViewHolder>>
): ListAdapter<DelegateItem, RecyclerView.ViewHolder>(DelegateItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return delegates[getItemViewType(position)].onBindViewHolder(holder, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return delegates.indexOfFirst {
            it.itemClass == getItem(position).javaClass
        }
    }
}