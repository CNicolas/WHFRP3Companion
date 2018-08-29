package com.nicolas.models.talent

enum class TalentCooldown {
    PASSIVE,
    TALENT,
    SESSION;

    companion object {
        operator fun get(ordinal: Int): TalentCooldown = TalentCooldown.values()[ordinal]
    }
}