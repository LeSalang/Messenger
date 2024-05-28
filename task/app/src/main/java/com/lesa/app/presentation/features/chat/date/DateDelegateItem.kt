package com.lesa.app.presentation.features.chat.date

import com.lesa.app.presentation.composite_adapter.DelegateItem
import java.util.Date

class DateDelegateItem(
    val date: Date,
) : DelegateItem {
    override val id: Any
        get() = date.time

    override val content: Any
        get() = date

    override fun compareToOther(other: DelegateItem): Boolean {
        return if (other is DateDelegateItem) {
            other.date == date
        } else {
            false
        }
    }
}