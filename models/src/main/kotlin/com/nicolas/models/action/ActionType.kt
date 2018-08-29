package com.nicolas.models.action

enum class ActionType {
    MELEE_ATTACK,
    RANGE_ATTACK,
    SUPPORT,
    SPELL,
    SUMMONING;

    companion object {
        operator fun get(ordinal: Int): ActionType = ActionType.values()[ordinal]
    }
}