package com.nicolas.whfrp3companion.shared.enums

import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.enums.Characteristic.*
import com.nicolas.whfrp3database.entities.player.enums.Race
import com.nicolas.whfrp3database.entities.player.enums.Race.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.ItemType

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