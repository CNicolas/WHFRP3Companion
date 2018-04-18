package com.nicolas.whfrp3companion.fragments.talents

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R

class TalentTypesFragment : Fragment() {
    @BindView(R.id.talent_types_list)
    lateinit var talentTypesListView: ListView

    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val resultingView: View = inflater.inflate(R.layout.fragment_talent_types, container, false)

        unbinder = ButterKnife.bind(this, resultingView)

        val talentTypesAdapter = TalentTypesAdapter(context!!)
        talentTypesListView.adapter = talentTypesAdapter

        return resultingView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
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