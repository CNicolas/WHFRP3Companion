package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.database.loadTalents
import com.nicolas.models.player.playerLinked.talent.Talent
import com.nicolas.playersheet.dtos.TalentSearch
import com.nicolas.playersheet.extensions.findTalents
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.talents.PlayerTalentsAdapter
import com.nicolas.whfrp3companion.playersheet.talents.TalentEditionMode
import com.nicolas.whfrp3companion.shared.DIALOG_TALENT_TYPE_TAG
import com.nicolas.whfrp3companion.shared.TALENTS_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.TALENT_EDITION_MODE_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.TalentsAdapter
import com.nicolas.whfrp3companion.shared.dialogs.TalentSearchDialog
import kotlinx.android.synthetic.main.activity_talents.*

class TalentsActivity : AppCompatActivity() {
    private lateinit var unbinder: Unbinder

    private lateinit var allTalents: List<Talent>
    private var talentSearch: TalentSearch? = null
    private var talentEditionMode: TalentEditionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talents)

        unbinder = ButterKnife.bind(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        talentsRecyclerView.layoutManager = LinearLayoutManager(this)

        allTalents = loadTalents(this)

        talentSearch = intent?.extras?.getSerializable(TALENTS_SEARCH_INTENT_ARGUMENT) as TalentSearch
        talentEditionMode = intent?.extras?.getSerializable(TALENT_EDITION_MODE_INTENT_ARGUMENT) as TalentEditionMode?

        val search = talentSearch
        val talents = if (search !== null) {
            applySearch(search)
        } else {
            allTalents
        }

        talentsRecyclerView.adapter = if (talentEditionMode != null) {
            createTalentsAdapter(talents)
        } else {
            TalentsAdapter(this, talents)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    @OnClick(R.id.search)
    fun search() {
        val talentSearchDialog =
                talentSearch
                        ?.let { TalentSearchDialog.newInstance(it) }
                        ?: TalentSearchDialog.newInstance()

        talentSearchDialog.show(supportFragmentManager, DIALOG_TALENT_TYPE_TAG)
    }

    private fun createTalentsAdapter(talents: List<Talent>): PlayerTalentsAdapter {
        val adapter = PlayerTalentsAdapter(this, talents, talentEditionMode!!)
//        adapter.addTalentListener(this)

        return adapter
    }

    private fun applySearch(talentSearch: TalentSearch): List<Talent> {
        return allTalents.findTalents(talentSearch.text, talentSearch.cooldown, talentSearch.talentType)
    }
}