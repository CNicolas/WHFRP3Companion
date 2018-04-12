package com.nicolas.whfrp3companion.playersheet.inventory

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Spinner
import butterknife.*
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.bind
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.GenericItem
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Item
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality
import com.nicolas.whfrp3database.extensions.addItem
import com.nicolas.whfrp3database.extensions.moveToItemType
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AddItemActivity : AppCompatActivity() {
    private val itemTypeView by bind<Spinner>(R.id.item_type)
    private val qualityView by bind<Spinner>(R.id.quality)

    private val armorLayout by bind<View>(R.id.armor_layout)
    private val defenseLabelView by bind<ImageView>(R.id.defense_label)
    private val defenseView by bind<NumberPicker>(R.id.defense)
    private val soakLabelView by bind<ImageView>(R.id.soak_label)
    private val soakView by bind<NumberPicker>(R.id.soak)

    private val expandableLayout by bind<View>(R.id.expandable_layout)
    private val usesLabelView by bind<ImageView>(R.id.uses_label)
    private val usesView by bind<NumberPicker>(R.id.uses)

    private val weaponLayout by bind<View>(R.id.weapon_layout)
    private val weaponTypeView by bind<Spinner>(R.id.weapon_type)
    private val damageLabelView by bind<ImageView>(R.id.damage_label)
    private val damageView by bind<NumberPicker>(R.id.damage)
    private val criticalLevelLabelView by bind<ImageView>(R.id.critical_level_label)
    private val criticalLevelView by bind<NumberPicker>(R.id.critical_level)

    private lateinit var unbinder: Unbinder

    private val armorViews
        get () = listOf(armorLayout, defenseLabelView, defenseView, soakLabelView, soakView)
    private val expandableViews
        get() = listOf(expandableLayout, usesLabelView, usesView)
    private val weaponViews
        get () = listOf(weaponLayout, weaponTypeView, damageLabelView, damageView, criticalLevelLabelView, criticalLevelView)

    private lateinit var playerFacade: PlayerFacade

    private lateinit var player: Player
    private var item: Item = GenericItem("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        unbinder = ButterKnife.bind(this)

        val playerName = intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT)
        playerFacade = PlayerFacade(this)
        doAsync {
            player = playerFacade.find(playerName)!!
        }

        itemTypeView.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, values().map { getString(it.labelId) })
        itemTypeView.setSelection(ItemType.GENERIC_ITEM.ordinal)

        qualityView.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, Quality.values().map { getString(it.labelId) })
        qualityView.setSelection(Quality.NORMAL.ordinal)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    @OnTextChanged(R.id.item_name,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun modifyItem(editable: Editable) {
        item.name = editable.trim().toString()
    }

    @OnItemSelected(R.id.item_type)
    fun selectItemType(spinner: Spinner, position: Int) {
        item = item.moveToItemType(ItemType[position])

        when (item.type) {
            ARMOR -> showArmorViews()
            EXPANDABLE -> showExpandableViews()
            GENERIC_ITEM -> showGenericItemViews()
            WEAPON -> showWeaponViews()
        }
    }

    @OnClick(R.id.save_item)
    fun save() {
        doAsync {
            player.addItem(item)
            player = playerFacade.update(player)

            uiThread {
                finish()
            }
        }
    }

    private fun showArmorViews() {
        armorViews.show()
        expandableViews.hide()
        weaponViews.hide()
    }

    private fun showExpandableViews() {
        armorViews.hide()
        expandableViews.show()
        weaponViews.hide()
    }

    private fun showGenericItemViews() {
        armorViews.hide()
        expandableViews.hide()
        weaponViews.hide()
    }

    private fun showWeaponViews() {
        armorViews.hide()
        expandableViews.hide()
        weaponViews.show()
    }

    private fun List<View>.show() = this.forEach { it.visibility = VISIBLE }
    private fun List<View>.hide() = this.forEach { it.visibility = GONE }
}