package com.nicolas.whfrp3companion.playersheet.state

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.PlayerFacade
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.doAsync
import java.lang.Math.abs

class StanceChangeListener(context: Context,
                           private val player: Player,
                           private val currentStanceView: TextView) : DiscreteSeekBar.OnProgressChangeListener {
    private val playerFacade = PlayerFacade(context)

    private val conservativeColor = ContextCompat.getColor(context, R.color.conservative)
    private val recklessColor = ContextCompat.getColor(context, R.color.reckless)
    private val neutralColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)

    override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
        seekBar.changeColorForStance(value)
        player.stance = value
        currentStanceView.text = abs(value).toString()

        doAsync {
            playerFacade.update(player)
        }
    }

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

        currentStanceView.setTextColor(colorStateList)
    }
}