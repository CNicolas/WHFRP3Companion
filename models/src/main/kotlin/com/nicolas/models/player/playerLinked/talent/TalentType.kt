package com.nicolas.models.player.playerLinked.talent

enum class TalentType {
    AFFINITY,
    CAREER,
    FAITH,
    ORDER,
    REPUTATION,
    TACTICS,
    TRICK;

    companion object {
        operator fun get(ordinal: Int): TalentType = TalentType.values()[ordinal]
    }
}