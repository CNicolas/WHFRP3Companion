package com.nicolas.whfrp3companion.shared

import android.widget.EditText

val EditText.intValue: Int
    get() = if (text.isNullOrBlank()) {
        0
    } else {
        text.toString().toInt()
    }