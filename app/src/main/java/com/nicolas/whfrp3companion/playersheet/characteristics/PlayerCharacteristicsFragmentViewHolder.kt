package com.nicolas.whfrp3companion.playersheet.characteristics

import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.hand.Hand
import com.nicolas.whfrp3database.entities.player.CharacteristicValue
import com.nicolas.whfrp3database.entities.player.Player
import org.jetbrains.anko.longToast

internal class PlayerCharacteristicsFragmentViewHolder(private val view: View) {
    @BindView(R.id.career)
    lateinit var career: EditText
    @BindView(R.id.rank)
    lateinit var rank: EditText
    @BindView(R.id.experience)
    lateinit var experience: EditText
    @BindView(R.id.max_experience)
    lateinit var maxExperience: EditText
    @BindView(R.id.max_wounds)
    lateinit var maxWounds: EditText
    @BindView(R.id.max_corruption)
    lateinit var maxCorruption: EditText

    @BindView(R.id.strength)
    lateinit var strength: EditText
    @BindView(R.id.strength_fortune)
    lateinit var strengthFortune: EditText
    @BindView(R.id.toughness)
    lateinit var toughness: EditText
    @BindView(R.id.toughness_fortune)
    lateinit var toughnessFortune: EditText
    @BindView(R.id.agility)
    lateinit var agility: EditText
    @BindView(R.id.agility_fortune)
    lateinit var agilityFortune: EditText
    @BindView(R.id.intelligence)
    lateinit var intelligence: EditText
    @BindView(R.id.intelligence_fortune)
    lateinit var intelligenceFortune: EditText
    @BindView(R.id.willpower)
    lateinit var willpower: EditText
    @BindView(R.id.willpower_fortune)
    lateinit var willpowerFortune: EditText
    @BindView(R.id.fellowship)
    lateinit var fellowship: EditText
    @BindView(R.id.fellowship_fortune)
    lateinit var fellowshipFortune: EditText

    @BindView(R.id.max_conservative)
    lateinit var maxConservative: EditText
    @BindView(R.id.max_reckless)
    lateinit var maxReckless: EditText
    @BindView(R.id.description)
    lateinit var description: EditText

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
//        view.context.startActivity(view.context.intentFor<DiceRollerActivity>(
//                HAND_INTENT_ARGUMENT to hand
//        ))
        view.context.longToast("$hand")
    }

    private val EditText.intValue: Int
        get() = if (text.isNullOrBlank()) {
            0
        } else {
            text.toString().toInt()
        }
}