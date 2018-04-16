package com.nicolas.whfrp3companion.playersheet.inventory

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnItemSelected
import butterknife.Unbinder
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.*
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3database.PlayerFacade
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType.*
import com.nicolas.whfrp3database.extensions.addItem
import com.nicolas.whfrp3database.extensions.removeItem
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ItemEditionActivity : AppCompatActivity() {
    private val itemNameEditText by bind<EditText>(R.id.item_name)
    private val itemTypeView by bind<Spinner>(R.id.item_type)
    private val encumbranceEditText by bind<EditText>(R.id.encumbrance)
    private val quantityEditText by bind<EditText>(R.id.quantity)
    private val qualitySpinner by bind<Spinner>(R.id.quality)
    private val descriptionEditText by bind<EditText>(R.id.description)

    private val armorLayout by bind<View>(R.id.armor_layout)
    private val armorTypeSpinner by bind<Spinner>(R.id.armor_type)
    private val defenseLabelView by bind<ImageView>(R.id.defense_label)
    private val defenseNumberPicker by bind<NumberPicker>(R.id.defense)
    private val soakLabelView by bind<ImageView>(R.id.soak_label)
    private val soakNumberPicker by bind<NumberPicker>(R.id.soak)

    private val expandableLayout by bind<View>(R.id.expandable_layout)
    private val usesLabelView by bind<ImageView>(R.id.uses_label)
    private val usesNumberPicker by bind<NumberPicker>(R.id.uses)

    private val weaponLayout by bind<View>(R.id.weapon_layout)
    private val weaponTypeSpinner by bind<Spinner>(R.id.weapon_type)
    private val weaponRangeSpinner by bind<Spinner>(R.id.range)
    private val damageLabelView by bind<ImageView>(R.id.damage_label)
    private val damageNumberPicker by bind<NumberPicker>(R.id.damage)
    private val criticalLevelLabelView by bind<ImageView>(R.id.critical_level_label)
    private val criticalLevelNumberPicker by bind<NumberPicker>(R.id.critical_level)

    private lateinit var unbinder: Unbinder

    private val armorViews
        get () = listOf(armorLayout, armorTypeSpinner, defenseLabelView, defenseNumberPicker, soakLabelView, soakNumberPicker)
    private val expandableViews
        get() = listOf(expandableLayout, usesLabelView, usesNumberPicker)
    private val weaponViews
        get () = listOf(weaponLayout, weaponTypeSpinner, weaponRangeSpinner, damageLabelView, damageNumberPicker, criticalLevelLabelView, criticalLevelNumberPicker)

    private lateinit var playerFacade: PlayerFacade

    private lateinit var player: Player
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        unbinder = ButterKnife.bind(this)

        val playerName = intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT)
        playerFacade = PlayerFacade(this)
        doAsync {
            player = playerFacade.find(playerName)!!
        }
        item = intent.extras.getSerializable(ITEM_EDIT_INTENT_ARGUMENT) as Item?

        itemTypeView.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, values().map { getString(it.labelId) })
        qualitySpinner.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, Quality.values().map { getString(it.labelId) })
        armorTypeSpinner.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, ArmorType.values().map { getString(it.labelId) })
        weaponTypeSpinner.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, WeaponType.values().map { getString(it.labelId) })
        weaponRangeSpinner.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, Range.values().map { getString(it.labelId) })

        if (item == null) {
            itemTypeView.setSelection(ItemType.GENERIC_ITEM.ordinal)
            qualitySpinner.setSelection(Quality.NORMAL.ordinal)
            armorTypeSpinner.setSelection(ArmorType.LEATHER.ordinal)
            weaponTypeSpinner.setSelection(WeaponType.SWORD.ordinal)
            weaponRangeSpinner.setSelection(Range.ENGAGED.ordinal)
        } else {
            fillViewsWithItem(item!!)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    @OnItemSelected(R.id.item_type)
    fun selectItemType(spinner: Spinner, position: Int) {
        when (ItemType[position]) {
            ARMOR -> showArmorViews()
            EXPANDABLE -> showExpandableViews()
            GENERIC_ITEM -> showGenericItemViews()
            WEAPON -> showWeaponViews()
        }
    }

    @OnClick(R.id.save_item)
    fun save() {
        doAsync {
            if (item != null) {
                player.removeItem(item!!)
            }

            player.addItem(createItemFromViews())
            player = playerFacade.update(player)

            uiThread {
                finish()
            }
        }
    }

    private fun fillViewsWithItem(item: Item) {
        itemNameEditText.setText(item.name)
        itemTypeView.setSelection(item.type.ordinal)
        quantityEditText.setText(item.quantity.toString())
        encumbranceEditText.setText(item.encumbrance.toString())
        qualitySpinner.setSelection(item.quality.ordinal)
        descriptionEditText.setText(item.description)

        when (item.type) {
            ARMOR -> {
                val armor = item as Armor

                armorTypeSpinner.setSelection(armor.subType.ordinal)
                defenseNumberPicker.value = armor.defense
                soakNumberPicker.value = armor.soak
            }
            EXPANDABLE -> {
                val expandable = item as Expandable

                usesNumberPicker.value = expandable.uses
            }
            WEAPON -> {
                val weapon = item as Weapon

                weaponTypeSpinner.setSelection(weapon.subType.ordinal)
                weaponRangeSpinner.setSelection(weapon.range.ordinal)
                damageNumberPicker.value = weapon.damage
                criticalLevelNumberPicker.value = weapon.criticalLevel
            }
            GENERIC_ITEM -> {
            }
        }
    }

    private fun createItemFromViews(): Item {
        return when (ItemType[itemTypeView.selectedItemPosition]) {
            ARMOR -> Armor(
                    itemNameEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    encumbranceEditText.intValue,
                    quantityEditText.intValue,
                    Quality[qualitySpinner.selectedItemPosition],
                    false,
                    ArmorType[armorTypeSpinner.selectedItemPosition],
                    soakNumberPicker.value,
                    defenseNumberPicker.value
            )
            EXPANDABLE -> Expandable(
                    itemNameEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    encumbranceEditText.intValue,
                    quantityEditText.intValue,
                    Quality[qualitySpinner.selectedItemPosition],
                    usesNumberPicker.value
            )
            GENERIC_ITEM -> GenericItem(
                    itemNameEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    encumbranceEditText.intValue,
                    quantityEditText.intValue,
                    Quality[qualitySpinner.selectedItemPosition]
            )
            WEAPON -> Weapon(
                    itemNameEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    encumbranceEditText.intValue,
                    quantityEditText.intValue,
                    Quality[qualitySpinner.selectedItemPosition],
                    false,
                    WeaponType[weaponTypeSpinner.selectedItemPosition],
                    damageNumberPicker.value,
                    criticalLevelNumberPicker.value,
                    Range[weaponRangeSpinner.selectedItemPosition]
            )
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
}