package com.nicolas.whfrp3companion.shared.viewModifications

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan

fun parseTemplatedText(context: Context, text: String): SpannableString {
    val imagedText = SpannableString(text)

    val regex = Regex("\\{([A-Z_]+)\\}")
    val matched = regex.findAll(text)
    matched.mapNotNull { it.groups }
            .forEach {
                val matchGroupToReplace = it[0]!!
                val enumName = it[1]!!.value

                val icon = TextIcon.valueOf(enumName)
                val drawable = ContextCompat.getDrawable(context, icon.drawableId)
                if (drawable != null) {
                    imagedText.putImageBetweenPositions(drawable,
                            matchGroupToReplace.range.start,
                            matchGroupToReplace.range.endInclusive + 1,
                            icon.alignment)
                }
            }

    return imagedText
}

private fun SpannableString.putImageBetweenPositions(drawable: Drawable, startPosition: Int, endPosition: Int, alignment: Int) {
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    val imageSpan = ImageSpan(drawable, alignment)

    setSpan(imageSpan, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}