package com.nicolas.whfrp3companion.playersheet.effects

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import com.nicolas.database.PlayerRepository
import com.nicolas.database.loadEffects
import com.nicolas.models.effect.Effect
import com.nicolas.models.extensions.addEffect
import com.nicolas.models.extensions.removeEffect
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.state.EffectListener
import com.nicolas.whfrp3companion.playersheet.state.PlayerEffectsAdapter
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.normalized
import kotlinx.android.synthetic.main.fragment_player_effects.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerEffectsFragment : Fragment(), EffectListener {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player
    private lateinit var allEffects: List<Effect>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val resultingView: View = inflater.inflate(R.layout.fragment_player_effects, container, false)

        val playerName = arguments!!.getString(PLAYER_NAME_INTENT_ARGUMENT)
        doAsync {
            player = playerRepository.find(playerName)!!

            allEffects = loadEffects(context!!)

            uiThread {
                updateEffectsList(allEffects)
            }
        }

        return resultingView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.activity_effects, menu)

        menu?.let {
            val searchMenuItem = menu.findItem(R.id.search_effect)

            val searchManager: SearchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val info = searchManager.getSearchableInfo(activity?.componentName)
            val searchView: SearchView? = searchMenuItem.actionView as SearchView?

            searchView?.let { _ ->
                searchView.setSearchableInfo(info)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        updateEffectsList(searchEffectByName(query))
                        return false
                    }

                    override fun onQueryTextChange(s: String): Boolean {
                        updateEffectsList(searchEffectByName(s))
                        return false
                    }
                })
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onAddEffect(effect: Effect) {
        doAsync {
            player = playerRepository.update(player.addEffect(effect))
        }
    }

    override fun onRemoveEffect(effect: Effect) {
        doAsync {
            player = playerRepository.update(player.removeEffect(effect))
        }
    }

    private fun updateEffectsList(effects: List<Effect>) {
        activity?.let {
            effects_list.adapter = PlayerEffectsAdapter(it, effects, player.effects, this)
        }
    }

    private fun searchEffectByName(searchText: String): List<Effect> {
        return allEffects.filter {
            it.name.normalized.contains(searchText.normalized, true)
        }
    }

    companion object {
        fun newInstance(playerName: String): PlayerEffectsFragment {
            val args = Bundle()
            args.putString(PLAYER_NAME_INTENT_ARGUMENT, playerName)

            val fragment = PlayerEffectsFragment()
            fragment.arguments = args

            return fragment
        }
    }
}