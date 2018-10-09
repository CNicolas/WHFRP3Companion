package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.nicolas.database.PlayerRepository
import com.nicolas.database.loadTalents
import com.nicolas.models.extensions.addTalent
import com.nicolas.models.extensions.findTalents
import com.nicolas.models.player.Player
import com.nicolas.models.talent.Talent
import com.nicolas.models.talent.TalentSearch
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.talents.PlayerTalentsAdapter
import com.nicolas.whfrp3companion.playersheet.talents.TalentListener
import com.nicolas.whfrp3companion.shared.DIALOG_TALENT_SEARCH_TAG
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.TALENTS_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.TalentsAdapter
import com.nicolas.whfrp3companion.shared.dialogs.TalentSearchDialog
import com.nicolas.whfrp3companion.shared.enums.PlayerElementEditionMode
import com.nicolas.whfrp3companion.shared.getView
import com.nicolas.whfrp3companion.shared.viewModifications.hide
import com.nicolas.whfrp3companion.shared.viewModifications.show
import kotlinx.android.synthetic.main.activity_talents.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class TalentsActivity : AppCompatActivity(), TalentListener {
    private lateinit var allTalents: List<Talent>
    private var talentSearch: TalentSearch? = null

    private val playerRepository by inject<PlayerRepository>()

    private var player: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talents)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        talents_recyclerview.layoutManager = LinearLayoutManager(this)

        allTalents = loadTalents(this)

        talentSearch = intent?.extras?.getSerializable(TALENTS_SEARCH_INTENT_ARGUMENT) as TalentSearch
        val playerName = intent?.extras?.getString(PLAYER_NAME_INTENT_ARGUMENT)

        setupViews(playerName)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    // region TalentListener

    override fun onAddTalent(talent: Talent) {
        doAsync {
            player?.let {
                player = playerRepository.update(it.addTalent(talent))
            }
        }
    }

    override fun onToggleTalentEquipment(talent: Talent) {
    }

    override fun onRemoveTalent(talent: Talent) {
    }

    // endregion

    private fun setupViews(playerName: String?) {
        val talents = talentSearch?.let { applySearch(it) } ?: allTalents

        doAsync {
            player = playerName?.let {
                playerRepository.find(playerName)
            }

            uiThread { activity ->
                if (talents.isEmpty()) {
                    no_talents.show()
                    talents_recyclerview.hide()
                } else {
                    talents_recyclerview.adapter = if (player != null) {
                        createTalentsAdapter(talents)
                    } else {
                        TalentsAdapter(activity, talents)
                    }

                    no_talents.hide()
                    talents_recyclerview.show()
                }
            }
        }

        getView<FloatingActionButton>(R.id.search)
                .setOnClickListener { search() }
    }

    private fun search() {
        val talentSearchDialog =
                talentSearch
                        ?.let { TalentSearchDialog.newInstance(it) }
                        ?: TalentSearchDialog.newInstance()

        talentSearchDialog.show(supportFragmentManager, DIALOG_TALENT_SEARCH_TAG)
    }

    private fun createTalentsAdapter(talents: List<Talent>): PlayerTalentsAdapter {
        return PlayerTalentsAdapter(this, talents, this, PlayerElementEditionMode.ADDABLE)
    }

    private fun applySearch(talentSearch: TalentSearch): List<Talent> {
        return allTalents.findTalents(talentSearch.text, talentSearch.cooldown, talentSearch.talentType)
    }
}