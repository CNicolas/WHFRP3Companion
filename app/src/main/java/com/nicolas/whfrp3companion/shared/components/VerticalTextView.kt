package com.nicolas.whfrp3companion.shared.components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView

abstract class VerticalTextView(context: Context, attrs: AttributeSet) : TextView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(canvas: Canvas) {
        val textPaint = paint
        textPaint.color = currentTextColor
        textPaint.drawableState = drawableState

        canvas.save()

        rotateCanvas(canvas)
        canvas.translate(compoundPaddingLeft.toFloat(), extendedPaddingTop.toFloat())

        layout.draw(canvas)
        canvas.restore()
    }

    abstract fun rotateCanvas(canvas: Canvas)
}