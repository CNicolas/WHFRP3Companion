package com.nicolas.models.player.playerLinked.talent

enum class TalentCooldown {
    PASSIVE,
    TALENT,
    SESSION;

    companion object {
        operator fun get(ordinal: Int): TalentCooldown = TalentCooldown.values()[ordinal]
    }
}