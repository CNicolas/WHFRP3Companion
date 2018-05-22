package com.nicolas.whfrp3companion.shared.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.nicolas.database.PlayerRepository
import com.nicolas.database.loadTalents
import com.nicolas.models.extensions.addTalent
import com.nicolas.models.player.Player
import com.nicolas.models.player.talent.Talent
import com.nicolas.playersheet.dtos.TalentSearch
import com.nicolas.playersheet.extensions.findTalents
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.playersheet.talents.PlayerTalentsAdapter
import com.nicolas.whfrp3companion.playersheet.talents.TalentEditionMode
import com.nicolas.whfrp3companion.playersheet.talents.TalentListener
import com.nicolas.whfrp3companion.shared.DIALOG_TALENT_TYPE_TAG
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.TALENTS_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.adapters.TalentsAdapter
import com.nicolas.whfrp3companion.shared.dialogs.TalentSearchDialog
import kotlinx.android.synthetic.main.activity_talents.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class TalentsActivity : AppCompatActivity(), TalentListener {
    private lateinit var unbinder: Unbinder

    private lateinit var allTalents: List<Talent>
    private var talentSearch: TalentSearch? = null

    private val playerRepository by inject<PlayerRepository>()

    private var player: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talents)

        unbinder = ButterKnife.bind(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        talentsRecyclerView.layoutManager = LinearLayoutManager(this)

        allTalents = loadTalents(this)

        talentSearch = intent?.extras?.getSerializable(TALENTS_SEARCH_INTENT_ARGUMENT) as TalentSearch
        val playerName = intent?.extras?.getString(PLAYER_NAME_INTENT_ARGUMENT)

        val search = talentSearch
        val talents = if (search !== null) {
            applySearch(search)
        } else {
            allTalents
        }
        doAsync {
            player = playerName?.let {
                playerRepository.find(playerName)
            }

            uiThread { activity ->
                talentsRecyclerView.adapter = if (player != null) {
                    createTalentsAdapter(talents)
                } else {
                    TalentsAdapter(activity, talents)
                }
            }
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

    private fun createTalentsAdapter(talents: List<Talent>): PlayerTalentsAdapter {
        return PlayerTalentsAdapter(this, talents, this, TalentEditionMode.ADDABLE)
    }

    private fun applySearch(talentSearch: TalentSearch): List<Talent> {
        return allTalents.findTalents(talentSearch.text, talentSearch.cooldown, talentSearch.talentType)
    }
}