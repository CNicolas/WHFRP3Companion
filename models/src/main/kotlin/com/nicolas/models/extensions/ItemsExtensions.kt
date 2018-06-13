package com.nicolas.models.extensions

import com.nicolas.models.item.*
import com.nicolas.models.item.enums.ItemType
import com.nicolas.models.item.enums.ItemType.*
import com.nicolas.models.item.enums.Range
import com.nicolas.models.player.Player

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

fun Player.getItemByName(name: String): Item? = items.firstOrNull { it.name == name }
fun Player.getEquipmentByName(name: String): Equipment? = items.firstOrNull { it.name == name } as? Equipment
fun Player.getArmorByName(name: String): Armor? = getArmors().firstOrNull { it.name == name }
fun Player.getExpandableByName(name: String): Expandable? = getExpandables().firstOrNull { it.name == name }
fun Player.getGenericItemByName(name: String): GenericItem? = getGenericItems().firstOrNull { it.name == name }
fun Player.getWeaponByName(name: String): Weapon? = getWeapons().firstOrNull { it.name == name }

fun Player.getEquippedArmors() = getArmors().filter { it.isEquipped }
fun Player.getEquippedWeapons() = getWeapons().filter { it.isEquipped }

fun Player.removeItem(item: Item): List<Item> {
    val mutableItems = items.toMutableList()
    mutableItems.remove(item)
    items = mutableItems.toList()

    return items
}

fun Player.removeItemByName(name: String): List<Item> {
    val item = getItemByName(name)

    return if (item != null) {
        removeItem(item)
    } else {
        items
    }
}

fun Player.removeAllItems() {
    items = listOf()
}

fun Player.getWeaponDamage(weapon: Weapon): Int {
    var weaponDamage = weapon.damage

    getEffectsApplyingToWeapons().forEach {
        it.damageModifier?.let { damageModifier ->
            weaponDamage += damageModifier
        }
    }

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

