package com.lesa.app.composite_adapter

interface DelegateItem {
    val id: Any
    val content: Any // TODO remove if unused
    fun compareToOther(other: DelegateItem): Boolean
}