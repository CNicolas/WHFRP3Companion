package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.action.effect.ActionEffects
import com.nicolas.models.player.enums.Stance
import com.nicolas.models.player.enums.Stance.*
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ACTION_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.STANCE_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.ActionEffectsAdapter
import com.nicolas.whfrp3companion.shared.enums.colorId
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
        setupViews()
        setupSideViews(dominantStance)

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.conservative_side -> {
                    setupSideViews(CONSERVATIVE)
                    true
                }
                R.id.reckless_side -> {
                    setupSideViews(RECKLESS)
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

    private fun setupViews() {
        action_type_imageview.setImageResource(action.type.drawableId)
        action_traits_textview.text = if (action.traits.isNotEmpty()) {
            action.traits.joinToString { getString(it.labelId) }
        } else {
            "-"
        }
        action_skills_textview.text = action.skillsString
        action.conditionsString?.let {
            action_conditions_textview.text = parseTemplatedText(this, it)
        } ?: {
            action_conditions_textview.visibility = View.GONE
        }()
        navigation.itemIconTintList = null
    }

    private fun setupSideViews(side: Stance?) {
        when (side) {
            RECKLESS -> fillViewsWithRecklessSide()
            else -> fillViewsWithConservativeSide()
        }
    }

    private fun fillViewsWithConservativeSide() {
        action_difficulty_textview.text = parseTemplatedText(this, action.conservativeSide.difficultyString)
        action_cooldown_textview.text = action.conservativeSide.cooldownString
        action.conservativeSide.effects?.let { setEffectsAdapter(it) }
        setNavigationColors(CONSERVATIVE)
    }

    private fun fillViewsWithRecklessSide() {
        action_difficulty_textview.text = parseTemplatedText(this, action.recklessSide.difficultyString)
        action_cooldown_textview.text = action.recklessSide.cooldownString
        action.recklessSide.effects?.let { setEffectsAdapter(it) }
        setNavigationColors(RECKLESS)
    }

    private fun setEffectsAdapter(actionEffects: ActionEffects) {
        actionEffectsListView.adapter = ActionEffectsAdapter(this, actionEffects)
    }

    private fun setNavigationColors(side: Stance) {
        val neutralColor = getColor(NEUTRAL.colorId)
        val (conservativeString, conservativeColor) = getString(CONSERVATIVE.labelId) to getColor(CONSERVATIVE.colorId)
        val (recklessString, recklessColor) = getString(RECKLESS.labelId) to getColor(RECKLESS.colorId)

        when (side) {
            CONSERVATIVE -> {
                setNavigationItemText(navigation.menu.findItem(R.id.reckless_side), recklessString, neutralColor)
                setNavigationItemText(navigation.menu.findItem(R.id.conservative_side), conservativeString, conservativeColor)
            }
            RECKLESS -> {
                setNavigationItemText(navigation.menu.findItem(R.id.conservative_side), conservativeString, neutralColor)
                setNavigationItemText(navigation.menu.findItem(R.id.reckless_side), recklessString, recklessColor)
            }
            else -> return
        }
    }

    private fun setNavigationItemText(menuItem: MenuItem, title: String, color: Int) {
        val itemColoredTitle = SpannableString(title)
        itemColoredTitle.setSpan(
                ForegroundColorSpan(color),
                0,
                title.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        menuItem.title = itemColoredTitle
    }

    private val ActionSide.difficultyString: String
        get() = when (difficulty) {
            null -> ""
            else -> difficulty?.joinToString(" ") { "{${it.textIcon}}" } ?: ""
        }
}
