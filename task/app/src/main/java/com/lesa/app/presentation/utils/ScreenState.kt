package com.lesa.app.presentation.utils

sealed interface ScreenState<out R> {
    data object Loading : ScreenState<Nothing>
    data class Content<out T>(val content: T) : ScreenState<T>
    data object Error : ScreenState<Nothing>
}