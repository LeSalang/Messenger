package com.lesa.app.presentation.features.people

import com.lesa.app.composite_adapter.DelegateItem
import com.lesa.app.presentation.features.people.model.UserUi

class UserDelegateItem(
    val user: UserUi,
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