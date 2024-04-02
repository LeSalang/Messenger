package com.lesa.app.people

import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.model.User

class UserDelegateItem(
    val user: User,
) : DelegateItem {
    override val id: Any
        get() = user.id

    override val content: Any
        get() = user

    override fun compareToOther(other: DelegateItem): Boolean {
        return if (other is UserDelegateItem) {
            other.user == user
        } else {
            false
        }
    }
}