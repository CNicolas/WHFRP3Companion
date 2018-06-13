package com.nicolas.models.item.enums

import com.nicolas.models.item.enums.WeaponCategory.*

enum class WeaponCategory {
    MELEE,
    RANGE,
    FIRE_ARM,
    REPEATING
}

enum class WeaponType(val category: WeaponCategory) {
    AXE(MELEE),
    TWO_HANDED_AXE(MELEE),
    DAGGER(MELEE),
    FLAIL(MELEE),
    HALBERD(MELEE),
    HAMMER(MELEE),
    TWO_HANDED_HAMMER(MELEE),
    MACE(MELEE),
    TWO_HANDED_MACE(MELEE),
    SPEAR(MELEE),
    STICK(MELEE),
    SWORD(MELEE),
    TWO_HANDED_SWORD(MELEE),
    WHIP(MELEE),

    BOW(RANGE),
    CROSSBOW(RANGE),
    ONE_HANDED_CROSSBOW(RANGE),
    SLINGSHOT(RANGE),

    HANDGUN(FIRE_ARM),
    RIFLE(FIRE_ARM),

    REPEATING_GUN(REPEATING),
    REPEATING_CROSSBOW(REPEATING);

    companion object {
        operator fun get(ordinal: Int): WeaponType = values()[ordinal]
    }
}