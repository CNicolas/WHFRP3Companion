package com.nicolas.whfrp3companion.playersheet.diceRoller

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

class SimpleProgressChangeListener(private val progressChanged: (newValue: Int) -> Unit) : DiscreteSeekBar.OnProgressChangeListener {

    override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
        progressChanged(value)
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
    }
}