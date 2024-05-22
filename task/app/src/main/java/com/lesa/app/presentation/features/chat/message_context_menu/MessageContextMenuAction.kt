package com.lesa.app.presentation.features.chat.message_context_menu

import com.lesa.app.R

enum class MessageContextMenuAction() : ContextMenuAction {
    ADD_REACTION {
        override val title = R.string.context_menu_add_reaction
        override val icon: Int = R.drawable.icon_emoji
        override var isAvailableForIncomingMessage = true
    },

    DELETE_MESSAGE {
        override val title = R.string.context_menu_delete_message
        override val icon: Int = R.drawable.icon_delete
        override var isAvailableForIncomingMessage = false
    },

    EDIT_MESSAGE {
        override val title = R.string.context_menu_edit_message
        override val icon: Int = R.drawable.icon_edit
        override var isAvailableForIncomingMessage = false
    },

    CHANGE_TOPIC {
        override val title = R.string.context_menu_change_topic
        override val icon: Int = R.drawable.icon_change
        override var isAvailableForIncomingMessage = false
    },

    COPY_MESSAGE {
        override val title = R.string.context_menu_copy_message
        override val icon: Int = R.drawable.icon_copy
        override var isAvailableForIncomingMessage = true
    };

    abstract var isAvailableForIncomingMessage: Boolean
}