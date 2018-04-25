package com.nicolas.whfrp3companion.playersheet.characteristics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnTextChanged
import butterknife.Unbinder
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3database.PlayerFacade
import org.jetbrains.anko.doAsync

class PlayerCharacteristicsFragment : Fragment() {
    private lateinit var views: PlayerCharacteristicsFragmentViewHolder
    private lateinit var unbinder: Unbinder

    private lateinit var playerFacade: PlayerFacade

    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.fragment_player_characteristics, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        playerFacade = PlayerFacade(context!!)
        player = playerFacade.find(playerName)!!

        views = PlayerCharacteristicsFragmentViewHolder(resultingView)
        views.fillViews(player)

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()

        views.destroy()
    }

    @Suppress("UNUSED_PARAMETER")
    @OnTextChanged(R.id.career,
            R.id.rank,
            R.id.experience,
            R.id.max_experience,
            R.id.max_wounds,
            R.id.max_corruption,
            R.id.strength,
            R.id.strength_fortune,
            R.id.toughness,
            R.id.toughness_fortune,
            R.id.agility,
            R.id.agility_fortune,
            R.id.intelligence,
            R.id.intelligence_fortune,
            R.id.willpower,
            R.id.willpower_fortune,
            R.id.fellowship,
            R.id.fellowship_fortune,
            R.id.max_conservative,
            R.id.max_reckless,
            R.id.description,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun save(editable: Editable) {
        doAsync {
            val partialPlayer = views.extractPlayerFromViews()

            player = playerFacade.update(
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