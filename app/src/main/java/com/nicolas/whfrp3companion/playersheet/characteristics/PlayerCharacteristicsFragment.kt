package com.nicolas.whfrp3companion.playersheet.characteristics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import butterknife.ButterKnife
import butterknife.OnTextChanged
import butterknife.Unbinder
import com.nicolas.whfrp3companion.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Race
import org.jetbrains.anko.doAsync

class PlayerCharacteristicsFragment : Fragment() {
    internal lateinit var views: PlayerCharacteristicsFragmentViewHolder
    private lateinit var unbinder: Unbinder

    private lateinit var playerFacade: PlayerFacade

    private lateinit var player: Player

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.fragment_player_characteristics, container, false)

        playerFacade = PlayerFacade(context!!)

        views = PlayerCharacteristicsFragmentViewHolder(resultingView)
        unbinder = ButterKnife.bind(this, resultingView)

        if (arguments != null) {
            val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
            player = playerFacade.find(playerName)!!

            views.fillViews(player)
            views.race.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    doAsync {
                        if (player.race != Race[position]) {
                            player = playerFacade.update(views.extractPlayerFromViews())
                        }
                    }
                }
            }
        }

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
            player = playerFacade.update(views.extractPlayerFromViews())
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