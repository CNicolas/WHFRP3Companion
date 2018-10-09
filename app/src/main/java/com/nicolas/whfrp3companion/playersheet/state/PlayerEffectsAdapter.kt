package com.nicolas.whfrp3companion.playersheet.state

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.nicolas.models.effect.Effect
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.getView
import com.nicolas.whfrp3companion.shared.viewModifications.parseTemplatedText
import org.jetbrains.anko.design.indefiniteSnackbar

class PlayerEffectsAdapter(context: Context,
                           private val effects: List<Effect>,
                           addedEffects: List<Effect>,
                           private val effectListener: EffectListener) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)
    private val addedEffects = addedEffects.toMutableList()

    override fun getCount(): Int = effects.size
    override fun getItem(position: Int) = effects[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, savedView: View?, parent: ViewGroup?): View {
        var view = savedView

        val holder: ViewHolder
        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = inflater.inflate(R.layout.list_element_player_effect, parent, false)
            holder = ViewHolder(view, effectListener,
                    addEffect = { effect -> addedEffects.add(effect) },
                    removeEffect = { effect -> addedEffects.remove(effect) })
            view!!.tag = holder
        }

        val effect = getItem(position)
        holder.setupViews(effect, addedEffects)

        return view
    }

    internal class ViewHolder(private val view: View,
                              private val effectListener: EffectListener,
                              private val addEffect: (Effect) -> Unit,
                              private val removeEffect: (Effect) -> Unit) : RecyclerView.ViewHolder(view) {

        private val effectNameCheckBox by view.bind<CheckBox>(R.id.effectNameCheckBox)

        private lateinit var effect: Effect

        init {
            setupViewsEvents(view)
        }

        fun setupViews(effect: Effect, addedEffects: List<Effect>) {
            this.effect = effect

            effectNameCheckBox.text = effect.name
            effectNameCheckBox.isChecked = addedEffects.contains(effect)
        }

        private fun setupViewsEvents(view: View) {
            view.getView<ImageButton>(R.id.effectDescriptionImageButton)
                    .setOnClickListener { showEffectDescription() }

            view.getView<CheckBox>(R.id.effectNameCheckBox)
                    .setOnClickListener { onToggleEffect(it as CheckBox) }
        }

        private fun showEffectDescription() {
            val styledText = parseTemplatedText(view.context, "${effect.name}:\n${effect.description}")

            val snackbar = view.indefiniteSnackbar("")
            snackbar.setAction(android.R.string.ok) { snackbar.dismiss() }

            val snackbarTextView = snackbar.view.findViewById(android.support.design.R.id.snackbar_text) as TextView
            snackbarTextView.text = styledText
            snackbarTextView.maxLines = 10
        }

        private fun onToggleEffect(view: CheckBox) {
            if (view.isChecked) {
                effectListener.onAddEffect(effect)
                addEffect(effect)
            } else {
                effectListener.onRemoveEffect(effect)
                removeEffect(effect)
            }
        }
    }
}
