package com.nicolas.whfrp3companion.playersheet.state

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.doAsync

class StanceChangeListener(context: Context, private val player: Player) : DiscreteSeekBar.OnProgressChangeListener {
    private val playerFacade = PlayerFacade(context)

    private val conservativeColor = ContextCompat.getColor(context, R.color.conservative)
    private val recklessColor = ContextCompat.getColor(context, R.color.reckless)
    private val neutralColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)

    override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
        seekBar.changeColorForStance(value)
        player.stance = value

        doAsync {
            playerFacade.update(player)
        }
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {}

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {}

    private fun DiscreteSeekBar.changeColorForStance(value: Int) {
        when {
            value < 0 -> {
                setRippleColor(ColorStateList.valueOf(conservativeColor))
                setScrubberColor(conservativeColor)
                setThumbColor(ColorStateList.valueOf(conservativeColor), conservativeColor)
                setTrackColor(conservativeColor)
            }
            value > 0 -> {
                setRippleColor(ColorStateList.valueOf(recklessColor))
                setScrubberColor(recklessColor)
                setThumbColor(ColorStateList.valueOf(recklessColor), recklessColor)
                setTrackColor(recklessColor)
            }
            else -> {
                setRippleColor(ColorStateList.valueOf(neutralColor))
                setScrubberColor(neutralColor)
                setThumbColor(ColorStateList.valueOf(neutralColor), neutralColor)
                setTrackColor(neutralColor)
            }
        }
    }
}