package com.nicolas.whfrp3database.entities.hand

enum class DifficultyLevel {
    NONE,
    EASY,
    MEDIUM,
    HARD,
    EXTREME,
    GODLIKE;

    val challengeDicesCount = this.ordinal
}