package com.nicolas.whfrp3companion.shared.enums

import com.nicolas.models.action.ActionType
import com.nicolas.models.action.ActionType.*
import com.nicolas.models.dice.DiceType
import com.nicolas.models.dice.DiceType.*
import com.nicolas.models.dice.Face
import com.nicolas.models.dice.Face.*
import com.nicolas.models.item.enums.WeaponType
import com.nicolas.models.item.enums.WeaponType.*
import com.nicolas.whfrp3companion.R
import com.nicolas.whfrp3companion.shared.viewModifications.TextIcon
import com.nicolas.whfrp3companion.shared.viewModifications.TextIcon.*

internal val Face.drawableId: Int
    get() = when (this) {
        SUCCESS -> R.drawable.ic_success_black_16
        BOON -> R.drawable.ic_boon_black_16
        DELAY -> R.drawable.ic_delay_black_16
        SIGMAR -> R.drawable.ic_sigmar_black_16
        FAILURE -> R.drawable.ic_failure_black_16
        BANE -> R.drawable.ic_bane_black_16
        CHAOS -> R.drawable.ic_chaos_black_16
        else -> R.drawable.transparent_button
    }

internal val WeaponType.drawableId: Int
    get() = when (this) {
        AXE -> R.drawable.ic_axe_black
        TWO_HANDED_AXE -> R.drawable.ic_two_handed_axe_black
        DAGGER -> R.drawable.ic_dagger_black
        FLAIL -> R.drawable.ic_flail_black
        HALBERD -> R.drawable.ic_halberd_black
        HAMMER -> R.drawable.ic_hammer_black
        TWO_HANDED_HAMMER -> R.drawable.ic_two_handed_hammer_black
        MACE -> R.drawable.ic_mace_black
        TWO_HANDED_MACE -> R.drawable.ic_two_handed_mace_black
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
        REPEATING_GUN -> R.drawable.ic_repeating_hanggun_black
        REPEATING_CROSSBOW -> R.drawable.ic_repeating_crossbow_black
        THROWING_DAGGER -> R.drawable.ic_dagger_black
        THROWING_AXE -> R.drawable.ic_axe_black
    }

internal val ActionType.drawableId: Int
    get() = when (this) {
        MELEE_ATTACK -> R.drawable.ic_sword_black
        RANGE_ATTACK -> R.drawable.ic_handgun_black
        SUPPORT -> R.drawable.ic_flag_black
        SPELL -> R.drawable.ic_spell_black
        BLESSING -> R.drawable.ic_blessing_black
    }

internal val DiceType.textIcon: TextIcon
    get() = when (this) {
        CHARACTERISTIC -> CHARACTERISTIC_DICE
        CONSERVATIVE -> CONSERVATIVE_DICE
        RECKLESS -> RECKLESS_DICE
        EXPERTISE -> EXPERTISE_DICE
        FORTUNE -> FORTUNE_DICE
        CHALLENGE -> CHALLENGE_DICE
        MISFORTUNE -> MISFORTUNE_DICE
    }
