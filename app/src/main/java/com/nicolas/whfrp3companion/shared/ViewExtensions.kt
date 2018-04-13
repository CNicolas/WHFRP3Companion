package com.nicolas.whfrp3companion.shared

import android.view.View
import android.widget.EditText

val EditText.intValue: Int
    get() = if (text.isNullOrBlank()) {
        0
    } else {
        text.toString().toInt()
    }

internal fun List<View>.show() = this.forEach { it.visibility = View.VISIBLE }
internal fun List<View>.hide() = this.forEach { it.visibility = View.GONE }