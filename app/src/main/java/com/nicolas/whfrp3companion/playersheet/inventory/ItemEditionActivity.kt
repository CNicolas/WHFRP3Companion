package com.nicolas.whfrp3companion.playersheet.inventory

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.nicolas.database.PlayerRepository
import com.nicolas.models.extensions.addItem
import com.nicolas.models.extensions.removeItem
import com.nicolas.models.item.*
import com.nicolas.models.item.enums.*
import com.nicolas.models.item.enums.ItemType.*
import com.nicolas.models.player.Player
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.ITEM_EDIT_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.PLAYER_NAME_INTENT_ARGUMENT
import com.nicolas.whfrp3companion.shared.enums.labels
import com.nicolas.whfrp3companion.shared.enums.sortedAndLabels
import com.nicolas.whfrp3companion.shared.viewModifications.hide
import com.nicolas.whfrp3companion.shared.viewModifications.intValue
import com.nicolas.whfrp3companion.shared.viewModifications.markRequired
import com.nicolas.whfrp3companion.shared.viewModifications.show
import kotlinx.android.synthetic.main.activity_item_edition.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.koin.android.ext.android.inject

class ItemEditionActivity : AppCompatActivity() {
    private val playerRepository by inject<PlayerRepository>()

    private lateinit var player: Player
    private var item: Item? = null

    private lateinit var armorTypesWithLabels: Pair<List<ArmorType>, List<String>>
    private lateinit var weaponTypesWithLabels: Pair<List<WeaponType>, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_edition)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val playerName = intent.extras.getString(PLAYER_NAME_INTENT_ARGUMENT)
        doAsync {
            player = playerRepository.find(playerName)!!
        }
        item = intent.extras.getSerializable(ITEM_EDIT_INTENT_ARGUMENT) as Item?

        setupViews()
        setupViewsEvents()
        fillViewsWithItem(item)
        checkItemType(GENERIC_ITEM)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // region Views Initialization
    private fun setupViews() {
        val qualitySpinnerValues = Quality.values().labels(this)
        armorTypesWithLabels = ArmorType.values().sortedAndLabels(this)
        weaponTypesWithLabels = WeaponType.values().sortedAndLabels(this)
        val rangeSpinnerValues = Range.values().labels(this)

        quality.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, qualitySpinnerValues)
        armor_type.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, armorTypesWithLabels.second)
        weapon_type.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, weaponTypesWithLabels.second)
        range.adapter = ArrayAdapter(this, R.layout.element_enum_spinner, rangeSpinnerValues)

        name_wrapper.markRequired()
    }

    private fun setupViewsEvents() {
        item_type.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.item_type_generic -> showGenericItemViews()
                R.id.item_type_armor -> showArmorViews()
                R.id.item_type_weapon -> showWeaponViews()
                R.id.item_type_expandable -> showExpandableViews()
            }
        }

        save_item.setOnClickListener { save() }
    }

    private fun fillViewsWithItem(item: Item?) {
        if (item == null) {
            item_type.check(R.id.item_type_generic)
            quantity.setText("1")
            encumbrance.setText("0")
            quality.setSelection(Quality.NORMAL.ordinal)
            armor_type.setSelection(0)
            weapon_type.setSelection(0)
            range.setSelection(Range.ENGAGED.ordinal)

            return
        }

        name.setText(item.name)
        checkItemType(item.type)
        quantity.setText(item.quantity.toString())
        encumbrance.setText(item.encumbrance.toString())
        quality.setSelection(item.quality.ordinal)
        description.setText(item.description)

        when (item.type) {
            ARMOR -> {
                val armor = item as Armor

                armor_type.setSelection(armor.subType.ordinal)
                defense.setText("${armor.defense}")
                soak.setText("${armor.soak}")
            }
            EXPANDABLE -> {
                val expandable = item as Expandable

                uses.setText("${expandable.uses}")
            }
            WEAPON -> {
                val weapon = item as Weapon

                weapon_type.setSelection(weapon.subType.ordinal)
                range.setSelection(weapon.range.ordinal)
                damage.setText("${weapon.damage}")
                critical_level.setText("${weapon.criticalLevel}")
            }
            GENERIC_ITEM -> {
            }
        }
    }
    // endregion

    private fun save() {
        doAsync {
            item?.let {
                player.removeItem(it)
            }

            val itemToAdd = createItemFromViews()
            val quantityToAdd = itemToAdd.quantity

            itemToAdd.quantity = 1
            (0 until quantityToAdd).forEach {
                player.addItem(itemToAdd)
            }
            player = playerRepository.update(player)

            uiThread {
                finish()
            }
        }
    }

    private fun createItemFromViews(): Item {
        return when (getItemType()) {
            ARMOR -> Armor(
                    name = name.text.toString(),
                    description = description.text.toString(),
                    encumbrance = encumbrance.intValue,
                    quantity = quantity.intValue,
                    quality = Quality[quality.selectedItemPosition],
                    isEquipped = (item as? Equipment)?.isEquipped ?: false,
                    subType = armorTypesWithLabels.first[armor_type.selectedItemPosition],
                    soak = soak.intValue,
                    defense = defense.intValue
            )
            EXPANDABLE -> Expandable(
                    name = name.text.toString(),
                    description = description.text.toString(),
                    encumbrance = encumbrance.intValue,
                    quantity = quantity.intValue,
                    quality = Quality[quality.selectedItemPosition],
                    uses = uses.intValue
            )
            GENERIC_ITEM -> GenericItem(
                    name = name.text.toString(),
                    description = description.text.toString(),
                    encumbrance = encumbrance.intValue,
                    quantity = quantity.intValue,
                    quality = Quality[quality.selectedItemPosition]
            )
            WEAPON -> Weapon(
                    name = name.text.toString(),
                    description = description.text.toString(),
                    encumbrance = encumbrance.intValue,
                    quantity = quantity.intValue,
                    quality = Quality[quality.selectedItemPosition],
                    isEquipped = (item as? Equipment)?.isEquipped ?: false,
                    subType = weaponTypesWithLabels.first[weapon_type.selectedItemPosition],
                    damage = damage.intValue,
                    criticalLevel = critical_level.intValue,
                    range = Range[range.selectedItemPosition]
            )
        }
    }

    private fun checkItemType(itemType: ItemType) {
        when (itemType) {
            GENERIC_ITEM -> item_type.check(R.id.item_type_generic)
            ARMOR -> item_type.check(R.id.item_type_armor)
            WEAPON -> item_type.check(R.id.item_type_weapon)
            EXPANDABLE -> item_type.check(R.id.item_type_expandable)
        }
    }

    private fun getItemType(): ItemType = when (item_type.checkedRadioButtonId) {
        R.id.item_type_generic -> GENERIC_ITEM
        R.id.item_type_armor -> ARMOR
        R.id.item_type_weapon -> WEAPON
        R.id.item_type_expandable -> EXPANDABLE
        else -> GENERIC_ITEM
    }

    private fun showGenericItemViews() {
        armor_type.hide()
        defense_wrapper.hide()
        soak_wrapper.hide()

        weapon_type.hide()
        damage_wrapper.hide()
        critical_level_wrapper.hide()
        range.hide()

        uses_wrapper.hide()
    }

    private fun showArmorViews() {
        armor_type.show()
        defense_wrapper.show()
        soak_wrapper.show()

        weapon_type.hide()
        damage_wrapper.hide()
        critical_level_wrapper.hide()
        range.hide()

        uses_wrapper.hide()
    }

    private fun showWeaponViews() {
        armor_type.hide()
        defense_wrapper.hide()
        soak_wrapper.hide()

        weapon_type.show()
        damage_wrapper.show()
        critical_level_wrapper.show()
        range.show()

        uses_wrapper.hide()
    }

    private fun showExpandableViews() {
        armor_type.hide()
        defense_wrapper.hide()
        soak_wrapper.hide()

        weapon_type.hide()
        damage_wrapper.hide()
        critical_level_wrapper.hide()
        range.hide()

        uses_wrapper.show()
    }
}