package com.lesa.app.composite_adapter

interface DelegateItem {
    val id: Any
    val content: Any
    fun compareToOther(other: DelegateItem): Boolean
}