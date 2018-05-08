package com.nicolas.whfrp3companion.shared.viewModifications

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan

fun parseTemplatedText(context: Context, text: String): SpannableString {
    val imagedText = SpannableString(text)

    imagedText.replaceDicesAndIcons(context, text)
    imagedText.replaceStance(context, text)

    return imagedText
}

private fun SpannableString.replaceDicesAndIcons(context: Context, text: String) {
    val regexIcons = Regex("\\{([A-Z_]+)\\}")

    regexIcons.findAll(text)
            .mapNotNull { it.groups }
            .forEach {
                val matchGroupToReplace = it[0]!!

                (1 until it.size).forEach { matchGroup ->
                    replaceCurrentMatched(context, matchGroupToReplace, it[matchGroup]!!.value)
                }
            }
}

private fun SpannableString.replaceStance(context: Context, text: String) {
    val regexStance = Regex("Jauge d'Attitude de Départ : (([PNT])([PNT])([PNT])([PNT])([PNT]))")

    regexStance.findAll(text)
            .mapNotNull { it.groups }
            .forEach {
                (2 until it.size).forEach { matchGroup ->
                    replaceCurrentMatched(context, it[matchGroup]!!, it[matchGroup]!!.value)
                }
            }
}

private fun SpannableString.replaceCurrentMatched(context: Context, matchGroupToReplace: MatchGroup, enumName: String) {
    val icon = TextIcon.valueOf(enumName)
    val drawable = ContextCompat.getDrawable(context, icon.drawableId)
    if (drawable != null) {
        putImageBetweenPositions(drawable,
                matchGroupToReplace.range.start,
                matchGroupToReplace.range.endInclusive + 1,
                icon.alignment)
    }
}

private fun SpannableString.putImageBetweenPositions(drawable: Drawable, startPosition: Int, endPosition: Int, alignment: Int) {
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    val imageSpan = ImageSpan(drawable, alignment)

    setSpan(imageSpan, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}