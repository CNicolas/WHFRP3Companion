package com.nicolas.whfrp3companion.playersheet.diceRoller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nicolas.database.PlayerRepository
import com.nicolas.models.player.Player
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.Specialization
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.SKILL_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.SPECIALIZATION_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.activity_dice_roller_skills.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.selector
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerDiceRollerSkillsActivity : AppCompatActivity() {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_roller_skills)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var playerName: String? = null
        if (intent?.extras !== null) {
            playerName = intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT)
        }

        playerName?.let { name ->
            doAsync {
                player = playerRepository.find(name)!!

                uiThread { activity ->
                    skillsListView.adapter = SimplePlayerSkillsAdapter(activity, player.skills) { selectSkill(it) }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()

        return true
    }

    private fun selectSkill(skill: Skill) {
        val title = getString(R.string.page_specializations)
        val specializations: List<Specialization> = skill.specializations.filter { it.isSpecialized }

        if (specializations.isEmpty()) {
            return closeActivityAfterSkillSelected(skill)
        }

        selector(title, specializations.map { it.name } + getString(R.string.none)) { _, index ->
            if (index >= specializations.size) {
                closeActivityAfterSkillSelected(skill)
            } else {
                closeActivityAfterSkillSelected(skill, specializations[index])
            }
        }
    }

    private fun closeActivityAfterSkillSelected(skill: Skill, specialization: Specialization? = null) {
        val resultingIntent = Intent()
        resultingIntent.putExtra(SKILL_INTENT_ARGUMENT, skill)
        resultingIntent.putExtra(SPECIALIZATION_INTENT_ARGUMENT, specialization)

        setResult(Activity.RESULT_OK, resultingIntent)
        finish()
    }
}