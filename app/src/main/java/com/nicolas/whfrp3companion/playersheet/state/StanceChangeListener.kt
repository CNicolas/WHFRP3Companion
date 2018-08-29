package com.nicolas.whfrp3companion.playersheet.state

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import com.nicolas.whfrp3companion.R
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

class StanceChangeListener(context: Context,
                           private val onColorChanged: (colorStateList: ColorStateList) -> Unit,
                           private val onStanceChanged: (stance: Int) -> Unit) : DiscreteSeekBar.OnProgressChangeListener {
    private val conservativeColor = ContextCompat.getColor(context, R.color.conservative)
    private val recklessColor = ContextCompat.getColor(context, R.color.reckless)
    private val neutralColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)

    override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) = onStanceChanged(value)

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {
        seekBar.changeColorForStance(seekBar.progress)
    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {
        seekBar.changeColorForStance(seekBar.progress)
    }

    private fun DiscreteSeekBar.changeColorForStance(value: Int) {
        val (colorStateList, color) = when {
            value < 0 -> ColorStateList.valueOf(conservativeColor) to conservativeColor
            value > 0 -> ColorStateList.valueOf(recklessColor) to recklessColor
            else -> ColorStateList.valueOf(neutralColor) to neutralColor
        }

        setRippleColor(colorStateList)
        setScrubberColor(colorStateList)
        setThumbColor(colorStateList, color)
        setTrackColor(colorStateList)

        onColorChanged(colorStateList)
    }
}