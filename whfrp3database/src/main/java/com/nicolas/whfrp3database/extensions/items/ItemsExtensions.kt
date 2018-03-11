package com.nicolas.whfrp3database.extensions.items

import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Range

fun Player.addItem(item: Item): List<Item> {
    val mutableItems = items.toMutableList()
    mutableItems.add(item)
    items = mutableItems.toList()

    return items
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
    val weaponDamage = weapon.damage ?: 0
    return when (weapon.range) {
        null -> weaponDamage
        Range.ENGAGED -> strength.value + weaponDamage
        else -> agility.value + weaponDamage
    }
}


fun List<Item>.getArmors(): List<Armor> =
        filter { it.type == ItemType.ARMOR }
                .map { it as Armor }

fun List<Item>.getExpandables(): List<Expandable> =
        filter { it.type == ItemType.EXPANDABLE }
                .map { it as Expandable }

fun List<Item>.getGenericItems(): List<GenericItem> =
        filter { it.type == ItemType.GENERIC_ITEM }
                .map { it as GenericItem }

fun List<Item>.getWeapons(): List<Weapon> =
        filter { it.type == ItemType.WEAPON }
                .map { it as Weapon }