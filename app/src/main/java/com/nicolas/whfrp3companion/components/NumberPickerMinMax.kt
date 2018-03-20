package com.nicolas.whfrp3companion.components

import android.content.Context
import android.util.AttributeSet
import android.widget.NumberPicker
import com.nicolas.whfrp3companion.R

/**
 * Extension of the standard NumberPicker, providing the xml attributes minValue and maxValue.
 */
class NumberPickerMinMax : NumberPicker {
    //region Constructors matching super
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        processAttributeSet(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        processAttributeSet(context, attrs)
    }
    //endregion

    private fun processAttributeSet(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.NumberPickerMinMax, 0, 0)

        minValue = a.getInteger(R.styleable.NumberPickerMinMax_minValue, context.resources.getInteger(R.integer.zero))
        maxValue = a.getInteger(R.styleable.NumberPickerMinMax_maxValue, context.resources.getInteger(R.integer.max_dice_number_picker))
    }
}