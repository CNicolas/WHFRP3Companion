package com.nicolas.whfrp3companion.playersheet.state

import android.app.ListActivity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.SearchView
import com.nicolas.models.player.effect.Effect
import com.nicolas.whfrp3companion.R
import kotlinx.android.synthetic.main.activity_effects.*

class EffectsActivity internal constructor() : ListActivity() {
    private val allEffects = listOf(
            Effect("First"),
            Effect("Second"),
            Effect("Third"),
            Effect("Fourth"),
            Effect("Fifth"))

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_effects)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        intent?.let {
            setIntent(intent)
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) {
        val effects = if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            searchEffectByName(query)
        } else {
            allEffects.toList()
        }

        effectsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, effects.map { it.name })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_effects, menu)

        menu?.let {
            val searchMenuItem = menu.findItem(R.id.search_effect)

            val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val info = searchManager.getSearchableInfo(componentName)
            val searchView: SearchView = searchMenuItem.actionView as SearchView

            searchView.setSearchableInfo(info)
            searchView.setIconifiedByDefault(false)
        }

        return true
    }

    private fun searchEffectByName(searchText: String): List<Effect> {
        return allEffects.filter { it.name.contains(searchText, true) }
    }
}
