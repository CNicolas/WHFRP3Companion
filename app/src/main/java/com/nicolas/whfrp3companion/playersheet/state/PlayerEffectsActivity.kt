package com.nicolas.whfrp3companion.playersheet.state

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.ArrayAdapter
import com.nicolas.database.PlayerRepository
import com.nicolas.database.loadEffects
import com.nicolas.models.extensions.addEffect
import com.nicolas.models.extensions.removeEffect
import com.nicolas.models.player.Player
import com.nicolas.models.player.effect.Effect
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import kotlinx.android.synthetic.main.activity_player_effects.*
import org.jetbrains.anko.act
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class PlayerEffectsActivity internal constructor() : AppCompatActivity(), EffectListener {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player
    private lateinit var allEffects: List<Effect>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_effects)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        doAsync {
            player = playerRepository.find(intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT))!!

            allEffects = loadEffects(act)

            uiThread {
                updateEffectsList(allEffects)
                longToast(player.name)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_effects, menu)

        menu?.let {
            val searchMenuItem = menu.findItem(R.id.search_effect)

            val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val info = searchManager.getSearchableInfo(componentName)
            val searchView: SearchView? = searchMenuItem.actionView as SearchView?

            searchView?.let {
                searchView.setSearchableInfo(info)
                searchView.setIconifiedByDefault(false)
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

        return super.onCreateOptionsMenu(menu)
    }

    override fun onAddEffect(effect: Effect) {
        player.addEffect(effect)
    }

    override fun onRemoveEffect(effect: Effect) {
        player.removeEffect(effect)
    }

    private fun updateEffectsList(effects: List<Effect>) {
        effectsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, effects.map { it.name })
    }

    private fun searchEffectByName(searchText: String): List<Effect> {
        return allEffects.filter { it.name.contains(searchText, true) }
    }
}
