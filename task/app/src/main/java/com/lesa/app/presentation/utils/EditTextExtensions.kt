package com.lesa.app.presentation.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.hideKeyboard() {
    val inputMethodManager: InputMethodManager =
        this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}