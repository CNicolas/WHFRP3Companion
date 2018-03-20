package com.nicolas.whfrp3companion.playersheet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.whfrp3companion.PLAYER_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.components.labelId
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Race

class PlayerCharacteristicsFragment : Fragment() {
    private lateinit var player: Player

    @BindView(R.id.player_name)
    lateinit var playerName: EditText
    @BindView(R.id.race)
    lateinit var race: Spinner

    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.fragment_player_characteristics, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        if (arguments != null) {
            if (arguments!!.getSerializable(PLAYER_INTENT_ARGUMENT) is Player) {
                player = arguments!!.getSerializable(PLAYER_INTENT_ARGUMENT) as Player
                playerName.setText(player.name)
            }
        }

        race.adapter = ArrayAdapter(context!!, R.layout.element_enum_spinner, Race.values().map { getString(it.labelId) })
        race.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                player.race = Race.HUMAN
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                player.race = Race.values()[position]
            }
        }

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    companion object {
        fun newInstance(player: Player): PlayerCharacteristicsFragment {
            val args = Bundle()
            args.putSerializable(PLAYER_INTENT_ARGUMENT, player)

            val fragment = PlayerCharacteristicsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}