package com.nicolas.whfrp3companion.shared.enums

import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.enums.Characteristic.*
import com.nicolas.whfrp3database.entities.player.enums.Race
import com.nicolas.whfrp3database.entities.player.enums.Race.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ArmorType.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Quality.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.Range.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.WeaponType.*

internal val Race.labelId: Int
    get() = when (this) {
        HUMAN -> R.string.human
        DWARF -> R.string.dwarf
        HIGH_ELF -> R.string.high_elf
        WOOD_ELF -> R.string.wood_elf
    }

internal val Characteristic.labelId: Int
    get() = when (this) {
        STRENGTH -> R.string.strength
        TOUGHNESS -> R.string.toughness
        AGILITY -> R.string.agility
        INTELLIGENCE -> R.string.intelligence
        WILLPOWER -> R.string.willpower
        FELLOWSHIP -> R.string.fellowship
    }

internal val ItemType.labelId: Int
    get() = when (this) {
        ItemType.ARMOR -> R.string.armor
        ItemType.EXPANDABLE -> R.string.expandable
        ItemType.GENERIC_ITEM -> R.string.generic_item
        ItemType.WEAPON -> R.string.weapon
    }
internal val ItemType.pluralLabelId: Int
    get() = when (this) {
        ItemType.ARMOR -> R.string.armors
        ItemType.EXPANDABLE -> R.string.expandables
        ItemType.GENERIC_ITEM -> R.string.generic_items
        ItemType.WEAPON -> R.string.weapons
    }

internal val Quality.labelId: Int
    get() = when (this) {
        LOW -> R.string.quality_low
        NORMAL -> R.string.quality_normal
        SUPERIOR -> R.string.quality_superior
        MAGIC -> R.string.quality_magic
    }

internal val ArmorType.labelId: Int
    get() = when (this) {
        HELMET -> R.string.armor_helmet
        HAT -> R.string.armor_hat
        PLATE -> R.string.armor_plate
        MAIL -> R.string.armor_mail
        LEATHER -> R.string.armor_leather
        ROBE -> R.string.armor_robe
        SHIELD -> R.string.armor_shield
    }

internal val WeaponType.labelId: Int
    get() = when (this) {
        DAGGER -> R.string.weapon_dagger
        FLAIL -> R.string.weapon_flail
        HALBERD -> R.string.weapon_halberd
        HAMMER -> R.string.weapon_hammer
        TWO_HANDED_HAMMER -> R.string.weapon_two_handed_hammer
        MACE -> R.string.weapon_mace
        TWO_HANDED_MACE -> R.string.weapon_two_handed_mace
        SPEAR -> R.string.weapon_spear
        STICK -> R.string.weapon_stick
        SWORD -> R.string.weapon_sword
        TWO_HANDED_SWORD -> R.string.weapon_two_handed_sword
        WHIP -> R.string.weapon_whip
        BOW -> R.string.weapon_bow
        CROSSBOW -> R.string.weapon_crossbow
        ONE_HANDED_CROSSBOW -> R.string.weapon_one_handed_crossbow
        SLINGSHOT -> R.string.weapon_sling
        HANDGUN -> R.string.weapon_handgun
        RIFLE -> R.string.weapon_rifle
        REPEATING_GUN -> R.string.weapon_repeating_gun
        REPEATING_CROSSBOW -> R.string.weapon_repeating_crossbow
    }
internal val WeaponType.drawableId: Int?
    get() = when (this) {
        DAGGER -> R.drawable.ic_dagger_black
//        FLAIL -> R.drawable.ic_sword_black
//        HALBERD -> R.drawable.ic_sword_black
        HAMMER -> R.drawable.ic_hammer_black
        TWO_HANDED_HAMMER -> R.drawable.ic_hammer_black
        MACE -> R.drawable.ic_mace_black
        TWO_HANDED_MACE -> R.drawable.ic_mace_black
        SPEAR -> R.drawable.ic_spear_black
        STICK -> R.drawable.ic_stick_black
        SWORD -> R.drawable.ic_sword_black
        TWO_HANDED_SWORD -> R.drawable.ic_two_handed_sword_black
        WHIP -> R.drawable.ic_whip_black
        BOW -> R.drawable.ic_bow_black
        CROSSBOW -> R.drawable.ic_crossbow_black
        ONE_HANDED_CROSSBOW -> R.drawable.ic_one_handed_crossbow_black
        SLINGSHOT -> R.drawable.ic_slingshot_black
        HANDGUN -> R.drawable.ic_handgun_black
        RIFLE -> R.drawable.ic_rifle_black
        REPEATING_GUN -> R.drawable.ic_handgun_black
        REPEATING_CROSSBOW -> R.drawable.ic_crossbow_black
        else -> null
    }

internal val Range.labelId: Int
    get() = when (this) {
        ENGAGED -> R.string.range_engaged
        SHORT -> R.string.range_short
        MEDIUM -> R.string.range_medium
        LONG -> R.string.range_long
        EXTREME -> R.string.range_extreme
    }