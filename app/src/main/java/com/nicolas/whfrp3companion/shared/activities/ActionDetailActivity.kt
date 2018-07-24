package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nicolas.models.action.Action
import com.nicolas.models.action.effect.ActionEffects
import com.nicolas.models.player.enums.Stance
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.STANCE_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.ActionEffectsAdapter
import com.nicolas.whfrp3companion.shared.enums.drawableId
import kotlinx.android.synthetic.main.activity_action_detail.*
import kotlinx.android.synthetic.main.content_action_side.*
import kotlinx.android.synthetic.main.toolbar.*

class ActionDetailActivity : AppCompatActivity() {
    private lateinit var action: Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_detail)
        setSupportActionBar(toolbar)

        action = intent.extras.getSerializable(ACTION_INTENT_ARGUMENT) as Action
        val dominantStance = intent.extras.getSerializable(STANCE_INTENT_ARGUMENT) as Stance?

        title = action.name
        setupViews(dominantStance)

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.conservative_side -> setupViews(Stance.CONSERVATIVE)
                R.id.reckless_side -> setupViews(Stance.RECKLESS)
                else -> false
            }
        }
    }

    private fun setupViews(side: Stance?): Boolean {
        actionTypeImageView.setImageResource(action.type.drawableId)
        actionTraitsTextView.text = action.traits.joinToString()
        actionSkillsTextView.text = action.skillsString
        actionConditionsTextView.text = action.conditionsString

        when (side) {
            Stance.RECKLESS -> fillViewsWithRecklessSide()
            Stance.CONSERVATIVE -> fillViewsWithConservativeSide()
            else -> fillViewsWithConservativeSide()
        }

        return true
    }

    private fun fillViewsWithConservativeSide() {
        actionCooldownTextView.text = action.conservativeSide.cooldown.toString()
        action.conservativeSide.effects?.let { setEffectsAdapter(it) }
    }

    private fun fillViewsWithRecklessSide() {
        actionCooldownTextView.text = action.recklessSide.cooldown.toString()
        action.recklessSide.effects?.let { setEffectsAdapter(it) }
    }

    private fun setEffectsAdapter(it: ActionEffects) {
        actionEffectsListView.adapter = ActionEffectsAdapter(this, it)
    }
}
