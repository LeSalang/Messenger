package com.lesa.app.compositeAdapter

interface DelegateItem {
    val id: Any
    val content: Any
    fun compareToOther(other: DelegateItem): Boolean
}