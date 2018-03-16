package com.nicolas.whfrp3companion

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.NumberPicker
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.whfrp3database.entities.hand.Hand
import org.jetbrains.anko.longToast

class DiceLauncherActivity : AppCompatActivity() {
    @BindView(R.id.characteristic_dice_picker)
    lateinit var characteristicDicePicker: NumberPicker
    @BindView(R.id.conservative_dice_picker)
    lateinit var conservativeDicePicker: NumberPicker
    @BindView(R.id.reckless_dice_picker)
    lateinit var recklessDicePicker: NumberPicker
    @BindView(R.id.expertise_dice_picker)
    lateinit var expertiseDicePicker: NumberPicker
    @BindView(R.id.fortune_dice_picker)
    lateinit var fortuneDicePicker: NumberPicker
    @BindView(R.id.challenge_dice_picker)
    lateinit var challengeDicePicker: NumberPicker
    @BindView(R.id.misfortune_dice_picker)
    lateinit var misfortuneDicePicker: NumberPicker

    private lateinit var unbinder: Unbinder

    private lateinit var hand: Hand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_launcher)

        hand = if (intent?.extras !== null) {
            intent.extras.getSerializable(HAND_INTENT_ARGUMENT) as Hand
        } else {
            Hand("")
        }

        unbinder = ButterKnife.bind(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    @OnClick(R.id.fab_launch_hand)
    fun launchHand() {
        val hand = Hand("No name",
                characteristicDicesCount = characteristicDicePicker.value,
                expertiseDicesCount = expertiseDicePicker.value,
                fortuneDicesCount = fortuneDicePicker.value,
                conservativeDicesCount = conservativeDicePicker.value,
                recklessDicesCount = recklessDicePicker.value,
                challengeDicesCount = challengeDicePicker.value,
                misfortuneDicesCount = misfortuneDicePicker.value)
        longToast(hand.toString())
    }

}
