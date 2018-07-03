package com.nicolas.whfrp3companion.shared.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import com.nicolas.models.action.effect.*
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.drawableId

class ActionEffectsAdapter(context: Context, private val effects: ActionEffects) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = effects.size
    override fun getItem(position: Int) = effects[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, savedView: View?, parent: ViewGroup?): View {
        var view = savedView

        val holder: ViewHolder
        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = inflater.inflate(R.layout.list_element_action_effect, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }

        val effect = getItem(position)
        holder.setupViews(effect)

        return view
    }

    internal class ViewHolder(view: View) {
        private var effect: ActionEffect? = null

        private val descriptionTextView by view.bind<TextView>(R.id.effectDescriptionTextView)
        private val face1ImageView by view.bind<ImageView>(R.id.effectFace1ImageView)
        private val face2ImageView by view.bind<ImageView>(R.id.effectFace2ImageView)
        private val face3ImageView by view.bind<ImageView>(R.id.effectFace3ImageView)
        private val face4ImageView by view.bind<ImageView>(R.id.effectFace4ImageView)


        init {
            ButterKnife.bind(this, view)
        }

        fun setupViews(effect: ActionEffect?) {
            this.effect = effect

            descriptionTextView.text = "Rien"

            this.effect?.let {
                setFaceDrawable(it.face.drawableId)
                setImagesVisibility(it.faceCount)
                descriptionTextView.text = it.third.toString()
            }
        }

        private fun setFaceDrawable(drawableId: Int) {
            face1ImageView.setImageResource(drawableId)
            face2ImageView.setImageResource(drawableId)
            face3ImageView.setImageResource(drawableId)
            face4ImageView.setImageResource(drawableId)
        }

        private fun setImagesVisibility(faceCount: Int) {
            face1ImageView.visibility = GONE
            face2ImageView.visibility = GONE
            face3ImageView.visibility = GONE
            face4ImageView.visibility = GONE

            when (faceCount) {
                1 -> face1ImageView.visibility = VISIBLE
                2 -> face2ImageView.visibility = VISIBLE
                3 -> face3ImageView.visibility = VISIBLE
                4 -> face4ImageView.visibility = VISIBLE
            }
        }
    }
}