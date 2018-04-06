package com.nicolas.whfrp3database.extensions

import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Range

fun Player.addItem(item: Item): List<Item> {
    val mutableItems = items.toMutableList()
    mutableItems.add(item)
    items = mutableItems.toList()

    return items
}

fun Player.getItemsOfType(type: ItemType) = when (type) {
    ARMOR -> getArmors()
    EXPANDABLE -> getExpandables()
    GENERIC_ITEM -> getGenericItems()
    WEAPON -> getWeapons()
}

fun Player.getArmors() = items.getArmors()
fun Player.getExpandables() = items.getExpandables()
fun Player.getGenericItems() = items.getGenericItems()
fun Player.getWeapons() = items.getWeapons()

fun Player.getArmorByName(name: String): Armor? = getArmors().firstOrNull { it.name == name }
fun Player.getExpandableByName(name: String): Expandable? = getExpandables().firstOrNull { it.name == name }
fun Player.getGenericItemByName(name: String): GenericItem? = getGenericItems().firstOrNull { it.name == name }
fun Player.getWeaponByName(name: String): Weapon? = getWeapons().firstOrNull { it.name == name }

fun Player.removeItem(item: Item): List<Item> {
    val mutableItems = items.toMutableList()
    mutableItems.remove(item)
    items = mutableItems.toList()

    return items
}

fun Player.removeAllItems() {
    items = listOf()
}

fun Player.getWeaponDamage(weapon: Weapon): Int {
    val weaponDamage = weapon.damage
    return when (weapon.range) {
        Range.ENGAGED -> strength.value + weaponDamage
        else -> agility.value + weaponDamage
    }
}

fun List<Item>.getArmors(): List<Armor> =
        filter { it.type == ARMOR }
                .map { it as Armor }

fun List<Item>.getExpandables(): List<Expandable> =
        filter { it.type == EXPANDABLE }
                .map { it as Expandable }

fun List<Item>.getGenericItems(): List<GenericItem> =
        filter { it.type == GENERIC_ITEM }
                .map { it as GenericItem }

fun List<Item>.getWeapons(): List<Weapon> =
        filter { it.type == WEAPON }
                .map { it as Weapon }

fun Item.moveToItemType(itemType: ItemType): Item =
        when (itemType) {
            ARMOR -> Armor(name, description, encumbrance, quantity, quality, subType, isEquipped, soak, defense, id)
            EXPANDABLE -> Expandable(name, description, encumbrance, quantity, quality, uses, id)
            GENERIC_ITEM -> GenericItem(name, description, encumbrance, quantity, quality, id)
            WEAPON -> Weapon(name, description, encumbrance, quantity, quality, subType, isEquipped, damage, criticalLevel, range, id)
        }

fun ItemType.create(): Item =
        when (this) {
            ARMOR -> Armor()
            EXPANDABLE -> Expandable()
            GENERIC_ITEM -> GenericItem()
            WEAPON -> Weapon()
        }