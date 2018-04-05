package com.nicolas.whfrp3companion.playersheet.characteristics

import android.view.View
import android.widget.EditText
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.DiceRollerActivity
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3database.entities.hand.Hand
import com.nicolas.whfrp3database.entities.player.CharacteristicValue
import com.nicolas.whfrp3database.entities.player.Player
import org.jetbrains.anko.intentFor

internal class PlayerCharacteristicsFragmentViewHolder(private val view: View) {
    private val career by view.bind<EditText>(R.id.career)
    private val rank by view.bind<EditText>(R.id.rank)
    private val experience by view.bind<EditText>(R.id.experience)
    private val maxExperience by view.bind<EditText>(R.id.max_experience)
    private val maxWounds by view.bind<EditText>(R.id.max_wounds)
    private val maxCorruption by view.bind<EditText>(R.id.max_corruption)

    private val strength by view.bind<EditText>(R.id.strength)
    private val strengthFortune by view.bind<EditText>(R.id.strength_fortune)
    private val toughness by view.bind<EditText>(R.id.toughness)
    private val toughnessFortune by view.bind<EditText>(R.id.toughness_fortune)
    private val agility by view.bind<EditText>(R.id.agility)
    private val agilityFortune by view.bind<EditText>(R.id.agility_fortune)
    private val intelligence by view.bind<EditText>(R.id.intelligence)
    private val intelligenceFortune by view.bind<EditText>(R.id.intelligence_fortune)
    private val willpower by view.bind<EditText>(R.id.willpower)
    private val willpowerFortune by view.bind<EditText>(R.id.willpower_fortune)
    private val fellowship by view.bind<EditText>(R.id.fellowship)
    private val fellowshipFortune by view.bind<EditText>(R.id.fellowship_fortune)

    private val maxConservative by view.bind<EditText>(R.id.max_conservative)
    private val maxReckless by view.bind<EditText>(R.id.max_reckless)
    private val description by view.bind<EditText>(R.id.description)

    private var unbinder: Unbinder = ButterKnife.bind(this, view)

    private var playerName: String = ""

    @OnClick(R.id.launch_strength)
    fun launchStrength() {
        launchDiceRollerActivity(CharacteristicValue(strength.intValue, strengthFortune.intValue).getHand("Strength"))
    }

    @OnClick(R.id.launch_toughness)
    fun launchToughness() {
        launchDiceRollerActivity(CharacteristicValue(toughness.intValue, toughnessFortune.intValue).getHand("Toughness"))
    }

    @OnClick(R.id.launch_agility)
    fun launchAgility() {
        launchDiceRollerActivity(CharacteristicValue(agility.intValue, agilityFortune.intValue).getHand("Agility"))
    }

    @OnClick(R.id.launch_intelligence)
    fun launchIntelligence() {
        launchDiceRollerActivity(CharacteristicValue(intelligence.intValue, intelligenceFortune.intValue).getHand("Intelligence"))
    }

    @OnClick(R.id.launch_willpower)
    fun launchWillpower() {
        launchDiceRollerActivity(CharacteristicValue(willpower.intValue, willpowerFortune.intValue).getHand("Willpower"))
    }

    @OnClick(R.id.launch_fellowship)
    fun launchFellowship() {
        launchDiceRollerActivity(CharacteristicValue(fellowship.intValue, fellowshipFortune.intValue).getHand("Fellowship"))
    }

    fun fillViews(player: Player) {
        playerName = player.name

        career.setText(player.careerName)
        rank.setText(player.rank.toString())
        experience.setText(player.experience.toString())
        maxExperience.setText(player.maxExperience.toString())
        maxWounds.setText(player.maxWounds.toString())
        maxCorruption.setText(player.maxCorruption.toString())

        strength.setText(player.strength.value.toString())
        strengthFortune.setText(player.strength.fortuneValue.toString())
        toughness.setText(player.toughness.value.toString())
        toughnessFortune.setText(player.toughness.fortuneValue.toString())
        agility.setText(player.agility.value.toString())
        agilityFortune.setText(player.agility.fortuneValue.toString())
        intelligence.setText(player.intelligence.value.toString())
        intelligenceFortune.setText(player.intelligence.fortuneValue.toString())
        willpower.setText(player.willpower.value.toString())
        willpowerFortune.setText(player.willpower.fortuneValue.toString())
        fellowship.setText(player.fellowship.value.toString())
        fellowshipFortune.setText(player.fellowship.fortuneValue.toString())

        maxConservative.setText(player.maxConservative.toString())
        maxReckless.setText(player.maxReckless.toString())

        description.setText(player.description)
    }

    fun extractPlayerFromViews(): Player = Player(
            name = playerName,
            description = description.text.toString(),
            strength = CharacteristicValue(strength.intValue, strengthFortune.intValue),
            toughness = CharacteristicValue(toughness.intValue, toughnessFortune.intValue),
            agility = CharacteristicValue(agility.intValue, agilityFortune.intValue),
            intelligence = CharacteristicValue(intelligence.intValue, intelligenceFortune.intValue),
            willpower = CharacteristicValue(willpower.intValue, willpowerFortune.intValue),
            fellowship = CharacteristicValue(fellowship.intValue, fellowshipFortune.intValue),
            careerName = career.text.toString(),
            rank = rank.intValue,
            experience = experience.intValue,
            maxExperience = maxExperience.intValue,
            maxConservative = maxConservative.intValue,
            maxReckless = maxReckless.intValue,
            maxWounds = maxWounds.intValue,
            maxCorruption = maxCorruption.intValue
    )

    fun destroy() {
        unbinder.unbind()
    }

    private fun launchDiceRollerActivity(hand: Hand) {
        view.context.startActivity(view.context.intentFor<DiceRollerActivity>(
                HAND_INTENT_ARGUMENT to hand
        ))
    }

    private val EditText.intValue: Int
        get() = if (text.isNullOrBlank()) {
            0
        } else {
            text.toString().toInt()
        }
}