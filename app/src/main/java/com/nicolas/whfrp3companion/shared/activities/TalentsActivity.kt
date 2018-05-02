package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ListView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.database.loadTalents
import com.nicolas.models.player.playerLinked.talent.Talent
import com.nicolas.playersheet.dtos.TalentSearch
import com.nicolas.playersheet.extensions.findTalents
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.TALENTS_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.TalentsAdapter
import com.nicolas.whfrp3companion.shared.bind
import org.jetbrains.anko.longToast

class TalentsActivity : AppCompatActivity() {
    private val talentsListView by bind<ListView>(R.id.talents)

    private lateinit var unbinder: Unbinder

    private lateinit var allTalents: List<Talent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talents)

        unbinder = ButterKnife.bind(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        allTalents = loadTalents(this)

        if (intent?.extras !== null) {
            val talentSearch = intent.extras.getSerializable(TALENTS_SEARCH_INTENT_ARGUMENT) as TalentSearch
            talentsListView.adapter = TalentsAdapter(this, applySearch(talentSearch))

            longToast(talentSearch.toString())
        } else {
            talentsListView.adapter = TalentsAdapter(this, allTalents)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    private fun applySearch(talentSearch: TalentSearch): List<Talent> {
        return allTalents.findTalents(talentSearch.text, talentSearch.cooldown, talentSearch.talentType)
    }
}