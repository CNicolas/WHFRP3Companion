package com.nicolas.whfrp3companion.shared.components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class DownToTopVerticalTextView(context: Context, attrs: AttributeSet) : VerticalTextView(context, attrs) {
    override fun rotateCanvas(canvas: Canvas) {
        canvas.translate(0f, height.toFloat())
        canvas.rotate(-90f)
    }
}