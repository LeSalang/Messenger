package com.lesa.app.presenation.chat.message

import com.lesa.app.compositeAdapter.DelegateItem

class MessageDelegateItem(
    val message: MessageView.Model
    ) : DelegateItem {
    override val id: Int
        get() = message.id

    override val content: Any
        get() = message

    override fun compareToOther(other: DelegateItem): Boolean {
        return if (other is MessageDelegateItem) {
            other.message == message
        } else {
            false
        }
    }

}
