package com.lesa.app.presenation.chat.date

import com.lesa.app.compositeAdapter.DelegateItem
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