package com.nicolas.whfrp3companion.playersheet.characteristics

import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.components.labelId
import com.nicolas.whfrp3database.entities.player.CharacteristicValue
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Race

internal class PlayerCharacteristicsFragmentViewHolder(view: View) {
    @BindView(R.id.player_name)
    lateinit var name: EditText
    @BindView(R.id.career)
    lateinit var career: EditText
    @BindView(R.id.rank)
    lateinit var rank: EditText
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
    @BindView(R.id.race)
    lateinit var race: Spinner
    @BindView(R.id.age)
    lateinit var age: EditText
    @BindView(R.id.size)
    lateinit var size: EditText
    @BindView(R.id.description)
    lateinit var description: EditText

    private var unbinder: Unbinder = ButterKnife.bind(this, view)

    init {
        race.adapter = ArrayAdapter(view.context!!, R.layout.element_enum_spinner, Race.values().map { view.context.getString(it.labelId) })
    }

    fun fillViews(player: Player) {
        name.setText(player.name)
        career.setText(player.careerName)
        rank.setText(player.rank.toString())
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

        race.setSelection(player.race.ordinal)
        age.setText(player.age?.toString())
        size.setText(player.size?.toString())
        description.setText(player.description)
    }

    fun getPlayer(): Player = Player(
            name = name.text.toString(),
            race = Race.values()[race.selectedItemPosition],
            age = age.intValue,
            size = size.intValue,
            description = description.text.toString(),
            strength = CharacteristicValue(strength.intValue, strengthFortune.intValue),
            toughness = CharacteristicValue(toughness.intValue, toughnessFortune.intValue),
            agility = CharacteristicValue(agility.intValue, agilityFortune.intValue),
            intelligence = CharacteristicValue(intelligence.intValue, intelligenceFortune.intValue),
            willpower = CharacteristicValue(willpower.intValue, willpowerFortune.intValue),
            fellowship = CharacteristicValue(fellowship.intValue, fellowshipFortune.intValue),
            careerName = career.text.toString(),
            rank = rank.intValue,
            maxConservative = maxConservative.intValue,
            maxReckless = maxReckless.intValue,
            maxWounds = maxWounds.intValue,
            maxCorruption = maxCorruption.intValue
    )

    fun destroy() {
        unbinder.unbind()
    }

    private val EditText.intValue: Int
        get() = if (text.isNullOrBlank()) {
            0
        } else {
            text.toString().toInt()
        }
}