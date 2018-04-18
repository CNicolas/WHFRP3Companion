package com.nicolas.whfrp3companion.shared.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.nicolas.playersheet.dtos.TalentSearch
import com.nicolas.whfrp3companion.R

class TalentSearchDialog() : DialogFragment() {
    private lateinit var unbinder: Unbinder

    private lateinit var talentSearch: TalentSearch

    constructor(talentSearch: TalentSearch) : this() {
        this.talentSearch = talentSearch
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_talent_search, null, false)

        builder.setView(view)
        builder.setTitle(R.string.search_menu_title)
        builder.setPositiveButton(R.string.search_menu_title, { _, _ -> dismiss() })

        val dialog = builder.create()
        unbinder = ButterKnife.bind(this, view)

        return dialog
    }
}
