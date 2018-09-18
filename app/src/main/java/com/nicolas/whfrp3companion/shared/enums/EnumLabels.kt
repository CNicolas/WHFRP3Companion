package com.nicolas.whfrp3companion.shared.enums

import com.nicolas.models.action.ActionType
import com.nicolas.models.action.ActionType.*
import com.nicolas.models.action.Trait
import com.nicolas.models.action.Trait.*
import com.nicolas.models.item.enums.*
import com.nicolas.models.item.enums.ArmorType.*
import com.nicolas.models.item.enums.ItemType.*
import com.nicolas.models.item.enums.Quality.*
import com.nicolas.models.item.enums.Range.*
import com.nicolas.models.item.enums.WeaponType.*
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.enums.Characteristic.*
import com.nicolas.models.player.enums.Race
import com.nicolas.models.player.enums.Race.*
import com.nicolas.models.talent.TalentCooldown
import com.nicolas.models.talent.TalentCooldown.*
import com.nicolas.models.talent.TalentType
import com.nicolas.models.talent.TalentType.*
import com.nicolas.whfrp3companion.R

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

internal val Characteristic.shortLabelId: Int
    get() = when (this) {
        STRENGTH -> R.string.strength_short
        TOUGHNESS -> R.string.toughness_short
        AGILITY -> R.string.agility_short
        INTELLIGENCE -> R.string.intelligence_short
        WILLPOWER -> R.string.willpower_short
        FELLOWSHIP -> R.string.fellowship_short
    }

internal val ItemType.labelId: Int
    get() = when (this) {
        ARMOR -> R.string.armor
        EXPANDABLE -> R.string.expandable
        GENERIC_ITEM -> R.string.generic_item
        WEAPON -> R.string.weapon
    }
internal val ItemType.pluralLabelId: Int
    get() = when (this) {
        ARMOR -> R.string.armors
        EXPANDABLE -> R.string.expandables
        GENERIC_ITEM -> R.string.generic_items
        WEAPON -> R.string.weapons
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
        AXE -> R.string.weapon_axe
        TWO_HANDED_AXE -> R.string.weapon_two_handed_axe
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

internal val Range.labelId: Int
    get() = when (this) {
        ENGAGED -> R.string.range_engaged
        SHORT -> R.string.range_short
        MEDIUM -> R.string.range_medium
        LONG -> R.string.range_long
        EXTREME -> R.string.range_extreme
    }

internal val TalentType.labelId: Int
    get() = when (this) {
        AFFINITY -> R.string.affinity
        CAREER -> R.string.career
        FAITH -> R.string.faith
        ORDER -> R.string.order
        REPUTATION -> R.string.reputation
        TACTICS -> R.string.tactics
        TRICK -> R.string.trick
    }

internal val TalentCooldown.labelId: Int
    get() = when (this) {
        PASSIVE -> R.string.cooldown_passive
        TALENT -> R.string.cooldown_talent
        SESSION -> R.string.cooldown_session
    }

internal val ActionType.labelId: Int
    get() = when (this) {
        MELEE_ATTACK -> R.string.melee_attack
        RANGE_ATTACK -> R.string.range_attack
        SUPPORT -> R.string.support
        SPELL -> R.string.spell
        SUMMONING -> R.string.summoning
    }

internal val Trait.labelId: Int
    get() = when (this) {
        BASIC -> R.string.trait_basic
        DEFENSE -> R.string.trait_defense
        CONTINUOUS -> R.string.trait_continuous
        TEAMWORK -> R.string.trait_teamwork
        KILLER -> R.string.trait_killer
        SWORD_WAY -> R.string.trait_sword_way
        RALLY -> R.string.trait_rally
        RANK_1 -> R.string.trait_rank_1
        RANK_2 -> R.string.trait_rank_2
        RANK_3 -> R.string.trait_rank_3
        RANK_4 -> R.string.trait_rank_4
        RANK_5 -> R.string.trait_rank_5
        LIGHT_ORDER -> R.string.trait_light_order
        CELESTIAL_ORDER -> R.string.trait_celestial_order
        GOLD_ORDER -> R.string.trait_gold_order
        JADE_ORDER -> R.string.trait_jade_order
        AMBER_ORDER -> R.string.trait_amber_order
        BRIGHT_ORDER -> R.string.trait_bright_order
        GREY_ORDER -> R.string.trait_grey_order
        AMETHYST_ORDER -> R.string.trait_amethyst_order
    }