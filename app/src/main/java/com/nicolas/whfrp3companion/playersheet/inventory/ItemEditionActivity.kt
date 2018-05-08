package com.nicolas.whfrp3companion.playersheet.inventory

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ArrayAdapter
import android.widget.Spinner
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnItemSelected
import butterknife.Unbinder
import com.nicolas.database.PlayerRepository
import com.nicolas.models.extensions.addItem
import com.nicolas.models.extensions.removeItem
import com.nicolas.models.player.Player
import com.nicolas.models.player.playerLinked.item.*
import com.nicolas.models.player.playerLinked.item.enums.*
import com.nicolas.models.player.playerLinked.item.enums.ItemType.*
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ITEM_EDIT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.enums.labelId
import com.nicolas.whfrp3companion.shared.viewModifications.hide
import com.nicolas.whfrp3companion.shared.viewModifications.intValue
import com.nicolas.whfrp3companion.shared.viewModifications.show
import kotlinx.android.synthetic.main.content_item_edition.*
import kotlinx.android.synthetic.main.part_armor_edition.*
import kotlinx.android.synthetic.main.part_expandable_edition.*
import kotlinx.android.synthetic.main.part_weapon_edition.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class ItemEditionActivity : AppCompatActivity() {
    private lateinit var unbinder: Unbinder

    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_edition)

        unbinder = ButterKnife.bind(this)

        val playerName = intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT)
        doAsync {
            player = playerRepository.find(playerName)!!
        }
        item = intent.extras.getSerializable(ITEM_EDIT_INTENT_ARGUMENT) as Item?

        itemTypeSpinner.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, values().map { getString(it.labelId) })
        qualitySpinner.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, Quality.values().map { getString(it.labelId) })
        armorTypeSpinner.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, ArmorType.values().map { getString(it.labelId) })
        weaponTypeSpinner.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, WeaponType.values().map { getString(it.labelId) })
        weaponRangeSpinner.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, Range.values().map { getString(it.labelId) })

        if (item == null) {
            itemTypeSpinner.setSelection(ItemType.GENERIC_ITEM.ordinal)
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

    @OnItemSelected(R.id.itemTypeSpinner)
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
            player = playerRepository.update(player)

            uiThread {
                finish()
            }
        }
    }

    private fun fillViewsWithItem(item: Item) {
        itemNameEditText.setText(item.name)
        itemTypeSpinner.setSelection(item.type.ordinal)
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
        return when (ItemType[itemTypeSpinner.selectedItemPosition]) {
            ARMOR -> Armor(
                    name = itemNameEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    encumbrance = encumbranceEditText.intValue,
                    quantity = quantityEditText.intValue,
                    quality = Quality[qualitySpinner.selectedItemPosition],
                    isEquipped = (item as? Equipment)?.isEquipped ?: false,
                    subType = ArmorType[armorTypeSpinner.selectedItemPosition],
                    soak = soakNumberPicker.value,
                    defense = defenseNumberPicker.value
            )
            EXPANDABLE -> Expandable(
                    name = itemNameEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    encumbrance = encumbranceEditText.intValue,
                    quantity = quantityEditText.intValue,
                    quality = Quality[qualitySpinner.selectedItemPosition],
                    uses = usesNumberPicker.value
            )
            GENERIC_ITEM -> GenericItem(
                    name = itemNameEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    encumbrance = encumbranceEditText.intValue,
                    quantity = quantityEditText.intValue,
                    quality = Quality[qualitySpinner.selectedItemPosition]
            )
            WEAPON -> Weapon(
                    name = itemNameEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    encumbrance = encumbranceEditText.intValue,
                    quantity = quantityEditText.intValue,
                    quality = Quality[qualitySpinner.selectedItemPosition],
                    isEquipped = (item as? Equipment)?.isEquipped ?: false,
                    subType = WeaponType[weaponTypeSpinner.selectedItemPosition],
                    damage = damageNumberPicker.value,
                    criticalLevel = criticalLevelNumberPicker.value,
                    range = Range[weaponRangeSpinner.selectedItemPosition]
            )
        }
    }

    private fun showArmorViews() {
        armorLayout.show()
        expandableLayout.hide()
        weaponLayout.hide()
    }

    private fun showExpandableViews() {
        armorLayout.hide()
        expandableLayout.show()
        weaponLayout.hide()
    }

    private fun showGenericItemViews() {
        armorLayout.hide()
        expandableLayout.hide()
        weaponLayout.hide()
    }

    private fun showWeaponViews() {
        armorLayout.hide()
        expandableLayout.hide()
        weaponLayout.show()
    }
}