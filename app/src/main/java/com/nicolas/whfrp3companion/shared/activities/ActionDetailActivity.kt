package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.action.effect.ActionEffects
import com.nicolas.models.player.enums.Stance
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.STANCE_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.ActionEffectsAdapter
import com.nicolas.whfrp3companion.shared.enums.drawableId
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.enums.textIcon
import com.nicolas.whfrp3companion.shared.viewModifications.parseTemplatedText
import kotlinx.android.synthetic.main.activity_action_detail.*
import kotlinx.android.synthetic.main.content_action_side.*
import kotlinx.android.synthetic.main.toolbar.*

class ActionDetailActivity : AppCompatActivity() {
    private lateinit var action: Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        action = intent.extras.getSerializable(ACTION_INTENT_ARGUMENT) as Action
        val dominantStance = intent.extras.getSerializable(STANCE_INTENT_ARGUMENT) as Stance?

        title = action.name
        setupViews(dominantStance)

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.conservative_side -> {
                    setupViews(Stance.CONSERVATIVE)
                    true
                }
                R.id.reckless_side -> {
                    setupViews(Stance.RECKLESS)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupViews(side: Stance?) {
        action_type_imageview.setImageResource(action.type.drawableId)
        action_traits_textview.text = action.traits.joinToString { getString(it.labelId) }
        action_skills_textview.text = action.skillsString
        action.conditionsString?.let {
            action_conditions_textview.text = parseTemplatedText(this, it)
        } ?: {
            action_conditions_textview.visibility = View.GONE
        }()

        when (side) {
            Stance.RECKLESS -> fillViewsWithRecklessSide()
            Stance.CONSERVATIVE -> fillViewsWithConservativeSide()
            else -> fillViewsWithConservativeSide()
        }

        navigation.itemIconTintList = null
    }

    private fun fillViewsWithConservativeSide() {
        action_difficulty_textview.text = parseTemplatedText(this, action.conservativeSide.difficultyString)
        action_cooldown_textview.text = action.conservativeSide.cooldownString
        action.conservativeSide.effects?.let { setEffectsAdapter(it) }
    }

    private fun fillViewsWithRecklessSide() {
        action_difficulty_textview.text = parseTemplatedText(this, action.recklessSide.difficultyString)
        action_cooldown_textview.text = action.recklessSide.cooldownString
        action.recklessSide.effects?.let { setEffectsAdapter(it) }
    }

    private fun setEffectsAdapter(actionEffects: ActionEffects) {
        actionEffectsListView.adapter = ActionEffectsAdapter(this, actionEffects)
    }

    private val ActionSide.difficultyString: String
        get() = when (difficulty) {
            null -> ""
            else -> difficulty?.joinToString(" ") { "{${it.textIcon}}" } ?: ""
        }
}
