package com.nicolas.models.action

enum class Trait {
    BASIC,
    DEFENSE,
    CONTINUOUS,
    TEAMWORK,
    ANCESTOR,
    KILLER,
    SWORD_WAY,
    RALLY,
    REACTION,
    RITUAL_DANCE,
    RANK_1,
    RANK_2,
    RANK_3,
    RANK_4,
    RANK_5,
    LIGHT_ORDER,
    CELESTIAL_ORDER,
    GOLD_ORDER,
    JADE_ORDER,
    AMBER_ORDER,
    BRIGHT_ORDER,
    GREY_ORDER,
    AMETHYST_ORDER;

    companion object {
        operator fun get(ordinal: Int): Trait = Trait.values()[ordinal]
    }
}