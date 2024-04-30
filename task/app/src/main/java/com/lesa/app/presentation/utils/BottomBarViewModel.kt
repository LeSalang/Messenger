package com.lesa.app.presentation.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

class BottomBarViewModel : ViewModel() {
    val isBottomBarShown = MutableSharedFlow<Boolean>()
}