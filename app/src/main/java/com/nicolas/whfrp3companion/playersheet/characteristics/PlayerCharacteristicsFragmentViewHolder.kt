package com.nicolas.whfrp3companion.playersheet.characteristics

import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import com.nicolas.models.hand.Hand
import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.PlayerDiceRollerActivity
import com.nicolas.whfrp3companion.shared.HAND_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.getView
import com.nicolas.whfrp3companion.shared.viewModifications.intValue
import org.jetbrains.anko.intentFor

internal class PlayerCharacteristicsFragmentViewHolder(private val player: Player,
                                                       private val view: View) {
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

    fun extractPlayerFromViews(): Player = Player(
            name = player.name,
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

    fun setupViews(player: Player) {
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

        setupViewEvents()
    }


    private fun setupViewEvents() {
        view.getView<ImageButton>(R.id.launch_strength).setOnClickListener { launchStrength() }
        view.getView<ImageButton>(R.id.launch_toughness).setOnClickListener { launchToughness() }
        view.getView<ImageButton>(R.id.launch_agility).setOnClickListener { launchAgility() }
        view.getView<ImageButton>(R.id.launch_intelligence).setOnClickListener { launchIntelligence() }
        view.getView<ImageButton>(R.id.launch_willpower).setOnClickListener { launchWillpower() }
        view.getView<ImageButton>(R.id.launch_fellowship).setOnClickListener { launchFellowship() }
    }

    private fun launchDiceRollerActivity(hand: Hand) {
        val context = view.context
        context.startActivity(context.intentFor<PlayerDiceRollerActivity>(
                PLAYER_NAME_INTENT_ARGUMENT to player.name,
                HAND_INTENT_ARGUMENT to hand
        ))
    }

    private fun launchStrength() =
            launchDiceRollerActivity(CharacteristicValue(strength.intValue, strengthFortune.intValue).getHand("Strength"))

    private fun launchToughness() =
            launchDiceRollerActivity(CharacteristicValue(toughness.intValue, toughnessFortune.intValue).getHand("Toughness"))

    private fun launchAgility() =
            launchDiceRollerActivity(CharacteristicValue(agility.intValue, agilityFortune.intValue).getHand("Agility"))

    private fun launchIntelligence() =
            launchDiceRollerActivity(CharacteristicValue(intelligence.intValue, intelligenceFortune.intValue).getHand("Intelligence"))

    private fun launchWillpower() =
            launchDiceRollerActivity(CharacteristicValue(willpower.intValue, willpowerFortune.intValue).getHand("Willpower"))

    private fun launchFellowship() =
            launchDiceRollerActivity(CharacteristicValue(fellowship.intValue, fellowshipFortune.intValue).getHand("Fellowship"))
}