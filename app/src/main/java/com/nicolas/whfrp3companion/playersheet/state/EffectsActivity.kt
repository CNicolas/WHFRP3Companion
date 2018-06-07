package com.nicolas.whfrp3companion.playersheet.state

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.ArrayAdapter
import com.nicolas.database.loadEffects
import com.nicolas.models.player.effect.Effect
import com.nicolas.whfrp3companion.R
import kotlinx.android.synthetic.main.activity_effects.*

class EffectsActivity internal constructor() : AppCompatActivity() {
    private lateinit var allEffects: List<Effect>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_effects)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        allEffects = loadEffects(this)
        updateEffectsList(allEffects)
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

    private fun updateEffectsList(effects: List<Effect>) {
        effectsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, effects.map { it.name })
    }

    private fun searchEffectByName(searchText: String): List<Effect> {
        return allEffects.filter { it.name.contains(searchText, true) }
    }
}
