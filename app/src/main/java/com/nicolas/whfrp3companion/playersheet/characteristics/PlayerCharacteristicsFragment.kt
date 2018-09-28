package com.nicolas.whfrp3companion.playersheet.characteristics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.database.PlayerRepository
import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.enums.Characteristic.*
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.advancedDiceRoller.PlayerAdvancedDiceRollerFragment
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.viewModifications.intValue
import kotlinx.android.synthetic.main.fragment_player_characteristics.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerCharacteristicsFragment : Fragment() {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.fragment_player_characteristics, container, false)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)

        doAsync {
            player = playerRepository.find(playerName)!!

            uiThread {
                setupViews(player)
                setupTextWatchersEvents()
            }
        }

        return resultingView
    }

    // region Initialization

    private fun setupViews(player: Player) {
        career.setText(player.careerName)
        rank.setText(player.rank.toString())
        experience.setText(player.experience.toString())
        max_experience.setText(player.maxExperience.toString())
        max_wounds.setText(player.maxWounds.toString())
        corruption.setText(player.corruption.toString())
        max_corruption.setText(player.maxCorruption.toString())

        strength.setText(player.strength.value.toString())
        strength_fortune.setText(player.strength.fortuneValue.toString())
        toughness.setText(player.toughness.value.toString())
        toughness_fortune.setText(player.toughness.fortuneValue.toString())
        agility.setText(player.agility.value.toString())
        agility_fortune.setText(player.agility.fortuneValue.toString())
        intelligence.setText(player.intelligence.value.toString())
        intelligence_fortune.setText(player.intelligence.fortuneValue.toString())
        willpower.setText(player.willpower.value.toString())
        willpower_fortune.setText(player.willpower.fortuneValue.toString())
        fellowship.setText(player.fellowship.value.toString())
        fellowship_fortune.setText(player.fellowship.fortuneValue.toString())

        max_conservative.setText(player.maxConservative.toString())
        max_reckless.setText(player.maxReckless.toString())

        description.setText(player.description)

        setupViewsEvents()
    }

    private fun setupTextWatchersEvents() {
        career.addTextChangedListener(playerSavingTextWatcher)
        rank.addTextChangedListener(playerSavingTextWatcher)
        experience.addTextChangedListener(playerSavingTextWatcher)
        max_experience.addTextChangedListener(playerSavingTextWatcher)
        max_wounds.addTextChangedListener(playerSavingTextWatcher)
        max_corruption.addTextChangedListener(playerSavingTextWatcher)
        strength.addTextChangedListener(playerSavingTextWatcher)
        strength_fortune.addTextChangedListener(playerSavingTextWatcher)
        toughness.addTextChangedListener(playerSavingTextWatcher)
        toughness_fortune.addTextChangedListener(playerSavingTextWatcher)
        agility.addTextChangedListener(playerSavingTextWatcher)
        agility_fortune.addTextChangedListener(playerSavingTextWatcher)
        intelligence.addTextChangedListener(playerSavingTextWatcher)
        intelligence_fortune.addTextChangedListener(playerSavingTextWatcher)
        willpower.addTextChangedListener(playerSavingTextWatcher)
        willpower_fortune.addTextChangedListener(playerSavingTextWatcher)
        fellowship.addTextChangedListener(playerSavingTextWatcher)
        fellowship_fortune.addTextChangedListener(playerSavingTextWatcher)
        max_conservative.addTextChangedListener(playerSavingTextWatcher)
        max_reckless.addTextChangedListener(playerSavingTextWatcher)
        description.addTextChangedListener(playerSavingTextWatcher)
    }

    private fun setupViewsEvents() {
        launch_strength.setOnClickListener { goToDiceRollerFragment(STRENGTH) }
        launch_toughness.setOnClickListener { goToDiceRollerFragment(TOUGHNESS) }
        launch_agility.setOnClickListener { goToDiceRollerFragment(AGILITY) }
        launch_intelligence.setOnClickListener { goToDiceRollerFragment(INTELLIGENCE) }
        launch_willpower.setOnClickListener { goToDiceRollerFragment(WILLPOWER) }
        launch_fellowship.setOnClickListener { goToDiceRollerFragment(FELLOWSHIP) }
    }

    // endregion

    private fun goToDiceRollerFragment(characteristic: Characteristic) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.playersheet_content_frame, PlayerAdvancedDiceRollerFragment.newInstance(player.name, characteristic))
                ?.commit()
    }

    private fun save() {
        doAsync {
            val partialPlayer = createPlayerFromViews()

            player = playerRepository.update(
                    player.copy(
                            name = partialPlayer.name,
                            description = partialPlayer.description,
                            strength = partialPlayer.strength,
                            toughness = partialPlayer.toughness,
                            agility = partialPlayer.agility,
                            intelligence = partialPlayer.intelligence,
                            willpower = partialPlayer.willpower,
                            fellowship = partialPlayer.fellowship,
                            careerName = partialPlayer.careerName,
                            rank = partialPlayer.rank,
                            experience = partialPlayer.experience,
                            maxExperience = partialPlayer.maxExperience,
                            maxConservative = partialPlayer.maxConservative,
                            maxReckless = partialPlayer.maxReckless,
                            maxWounds = partialPlayer.maxWounds,
                            corruption = partialPlayer.corruption,
                            maxCorruption = partialPlayer.maxCorruption
                    )
            )
        }
    }

    private fun createPlayerFromViews(): Player = Player(
            name = player.name,
            description = description.text.toString(),
            strength = CharacteristicValue(strength.intValue, strength_fortune.intValue),
            toughness = CharacteristicValue(toughness.intValue, toughness_fortune.intValue),
            agility = CharacteristicValue(agility.intValue, agility_fortune.intValue),
            intelligence = CharacteristicValue(intelligence.intValue, intelligence_fortune.intValue),
            willpower = CharacteristicValue(willpower.intValue, willpower_fortune.intValue),
            fellowship = CharacteristicValue(fellowship.intValue, fellowship_fortune.intValue),
            careerName = career.text.toString(),
            rank = rank.intValue,
            experience = experience.intValue,
            maxExperience = max_experience.intValue,
            maxConservative = max_conservative.intValue,
            maxReckless = max_reckless.intValue,
            maxWounds = max_wounds.intValue,
            corruption = corruption.intValue,
            maxCorruption = max_corruption.intValue
    )

    private val playerSavingTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) = save()
        }

    companion object {
        fun newInstance(playerName: String): PlayerCharacteristicsFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerCharacteristicsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}