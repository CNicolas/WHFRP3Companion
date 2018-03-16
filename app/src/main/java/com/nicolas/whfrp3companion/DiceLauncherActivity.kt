package com.nicolas.whfrp3companion

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import org.jetbrains.anko.longToast

class DiceLauncherActivity : AppCompatActivity() {
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_launcher)

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
        longToast("You wanted to launch something, right ?")
    }

}
