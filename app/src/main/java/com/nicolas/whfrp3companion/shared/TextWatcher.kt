package com.nicolas.whfrp3companion.shared

import android.text.Editable
import android.text.TextWatcher

internal fun createTextWatcher(afterTextChanged: (s: Editable) -> Unit): TextWatcher =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) = afterTextChanged(s)
        }