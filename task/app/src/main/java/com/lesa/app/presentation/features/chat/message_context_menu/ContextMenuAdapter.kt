package com.lesa.app.presentation.features.chat.message_context_menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lesa.app.databinding.ItemContextMenuBinding

class ContextMenuAdapter<Action : ContextMenuAction>(
    private val menuActionList: List<Action>,
    private val onMenuItemClick: (Action) -> Unit
) : RecyclerView.Adapter<ContextMenuAdapter.ViewHolder<Action>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder<Action> {
        return ViewHolder(
            binding = ItemContextMenuBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onMenuItemClick = onMenuItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder<Action>, position: Int) {
        return holder.bind(menuActionList[position])
    }

    override fun getItemCount(): Int {
        return menuActionList.size
    }

    class ViewHolder<Action : ContextMenuAction>(
        val binding: ItemContextMenuBinding,
        val onMenuItemClick: (Action) -> Unit
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(menuAction: Action) {
            binding.contextMenuIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    menuAction.icon
                )
            )
            binding.contextMenuName.text = ContextCompat.getString(
                itemView.context,
                menuAction.title
            )
            itemView.setOnClickListener {
                onMenuItemClick(menuAction)
            }
        }
    }
}