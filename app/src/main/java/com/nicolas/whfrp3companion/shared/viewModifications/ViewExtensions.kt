package com.nicolas.whfrp3companion.shared.viewModifications

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText

val EditText.intValue: Int
    get() = if (text.isNullOrBlank()) {
        0
    } else {
        text.toString().toInt()
    }

internal fun View.show() {
    visibility = VISIBLE
}

internal fun View.hide() {
    visibility = GONE
}

internal fun List<View>.show() = forEach { it.visibility = View.VISIBLE }
internal fun List<View>.hide() = forEach { it.visibility = View.GONE }