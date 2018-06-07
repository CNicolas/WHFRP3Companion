package com.nicolas.whfrp3companion.playersheet.state

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import butterknife.ButterKnife
import butterknife.OnClick
import com.nicolas.models.player.effect.Effect
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import org.jetbrains.anko.design.longSnackbar

class PlayerEffectsAdapter(context: Context,
                           private val effects: List<Effect>,
                           addedEffects: List<Effect>,
                           private val effectListener: EffectListener) : RecyclerView.Adapter<PlayerEffectsAdapter.ViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private val addedEffects = addedEffects.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.list_element_player_talent, parent, false)

        return ViewHolder(view, effectListener,
                addEffect = { effect -> addedEffects.add(effect) },
                removeEffect = { effect -> addedEffects.remove(effect) })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setupViews(effects[position], addedEffects)
    }

    override fun getItemCount(): Int = effects.size

    class ViewHolder(private val view: View,
                     private val effectListener: EffectListener,
                     private val addEffect: (Effect) -> Unit,
                     private val removeEffect: (Effect) -> Unit) : RecyclerView.ViewHolder(view) {

        private val effectNameCheckBox by view.bind<CheckBox>(R.id.effectNameCheckBox)
//        private val effectDescriptionImageButton by view.bind<ImageButton>(R.id.effectDescriptionImageButton)

        private lateinit var effect: Effect

        init {
            ButterKnife.bind(this, view)
        }

        fun setupViews(effect: Effect, addedEffects: List<Effect>) {
            this.effect = effect

            effectNameCheckBox.text = effect.name

            addedEffects.find { it.name == effect.name }?.let {
                this.effectNameCheckBox.isChecked = true
            }
        }

        @OnClick(R.id.effectDescriptionImageButton)
        fun showEffectDescription() {
            longSnackbar(view, effect.description).show()
        }

        @OnClick(R.id.effectNameCheckBox)
        fun onToggleEffect(view: CheckBox) {
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
