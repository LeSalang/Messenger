package com.lesa.app.presentation.utils

sealed interface LceState<out R> {
    data object Idle : LceState<Nothing>
    data object Loading : LceState<Nothing>
    data class Content<out T>(val content: T) : LceState<T>
    data object Error : LceState<Nothing>
}