package com.nicolas.whfrp3companion.playersheet.characteristics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.nicolas.database.PlayerRepository
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.getView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerCharacteristicsFragment : Fragment() {
    private lateinit var views: PlayerCharacteristicsFragmentViewHolder
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
                views = PlayerCharacteristicsFragmentViewHolder(resultingView)
                views.setupViews(player)
            }
        }

        setupTextWatchersEvents(resultingView)

        return resultingView
    }

    private fun setupTextWatchersEvents(view: View) {
        view.getView<EditText>(R.id.career).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.rank).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.experience).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.max_experience).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.max_wounds).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.max_corruption).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.strength).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.strength_fortune).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.toughness).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.toughness_fortune).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.agility).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.agility_fortune).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.intelligence).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.intelligence_fortune).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.willpower).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.willpower_fortune).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.fellowship).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.fellowship_fortune).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.max_conservative).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.max_reckless).addTextChangedListener(playerSavingTextWatcher)
        view.getView<EditText>(R.id.description).addTextChangedListener(playerSavingTextWatcher)
    }

    @Suppress("UNUSED_PARAMETER")
    fun save(editable: Editable) {
        doAsync {
            val partialPlayer = views.extractPlayerFromViews()

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
                            maxCorruption = partialPlayer.maxCorruption
                    )
            )
        }
    }

    private val playerSavingTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) = save(s)
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