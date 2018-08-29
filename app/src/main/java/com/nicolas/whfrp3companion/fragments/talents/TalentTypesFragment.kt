package com.nicolas.whfrp3companion.fragments.talents

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.nicolas.models.talent.TalentSearch
import com.nicolas.models.talent.TalentType
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.DIALOG_TALENT_SEARCH_TAG
import com.nicolas.whfrp3companion.shared.TALENTS_SEARCH_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.activities.TalentsActivity
import com.nicolas.whfrp3companion.shared.dialogs.TalentSearchDialog
import com.nicolas.whfrp3companion.shared.getView
import kotlinx.android.synthetic.main.fragment_talent_types.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread

class TalentTypesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_talent_types, container, false)

        setupViewEvents(resultingView)

        doAsync {
            val talentTypesAdapter = TalentTypesAdapter(context!!)

            uiThread {
                talentTypesListView.adapter = talentTypesAdapter
            }
        }

        return resultingView
    }

    private fun setupViewEvents(view: View) {
        view.getView<ListView>(R.id.talentTypesListView)
                .setOnItemClickListener { _, _, position, _ -> onTalentTypeClick(position) }

        view.getView<FloatingActionButton>(R.id.searchTalentFAB)
                .setOnClickListener { openTalentSearchDialog() }
    }

    private fun onTalentTypeClick(position: Int) {
        activity?.let {
            startActivity(it.intentFor<TalentsActivity>(
                    TALENTS_SEARCH_INTENT_ARGUMENT to TalentSearch(text = "", talentType = TalentType[position], cooldown = null)
            ))
        }
    }

    private fun openTalentSearchDialog() {
        val talentSearchDialog = TalentSearchDialog.newInstance()
        talentSearchDialog.show(activity?.supportFragmentManager, DIALOG_TALENT_SEARCH_TAG)
    }

    companion object {
        fun newInstance(): TalentTypesFragment {
            val args = Bundle()
            val fragment = TalentTypesFragment()
            fragment.arguments = args

            return fragment
        }
    }
}