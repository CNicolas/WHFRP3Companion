package com.nicolas.whfrp3companion.shared.viewModifications

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import java.util.regex.Pattern

fun parseTemplatedText(context: Context, text: String): SpannableString {
    val imagedText = SpannableString(text)

    val pattern = Pattern.compile("\\{([A-Z_]+)}")
    val matcher = pattern.matcher(text)
    while (matcher.find()) {
        val icon = TextIcon.valueOf(matcher.group(1))

        val drawable = ContextCompat.getDrawable(context, icon.drawableId)
        if (drawable != null) {
            putImageInStringAtPosition(imagedText, drawable, matcher.start(), matcher.end(), icon.alignment)
        }
    }

    return imagedText
}

private fun putImageInStringAtPosition(spannableString: SpannableString, drawable: Drawable, startPosition: Int, endPosition: Int, alignment: Int) {
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    val imageSpan = ImageSpan(drawable, alignment)

    spannableString.setSpan(imageSpan, startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}