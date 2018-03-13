package com.nicolas.whfrp3database.tables

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

internal const val PLAYER_TABLE_NAME = "Player"

internal fun createPlayerTable(db: SQLiteDatabase) {
    db.createTable(PLAYER_TABLE_NAME, true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE,
            "name" to TEXT + UNIQUE,
            "race" to TEXT,
            "age" to INTEGER,
            "size" to INTEGER,

            // region CHARACTERISTICS
            "strength" to INTEGER,
            "strengthFortune" to INTEGER,
            "toughness" to INTEGER,
            "toughnessFortune" to INTEGER,
            "agility" to INTEGER,
            "agilityFortune" to INTEGER,
            "intelligence" to INTEGER,
            "intelligenceFortune" to INTEGER,
            "willpower" to INTEGER,
            "willpowerFortune" to INTEGER,
            "fellowship" to INTEGER,
            "fellowshipFortune" to INTEGER,
            // endregion

            // region STATE
            "careerName" to TEXT,
            "rank" to INTEGER,
            "availableExperience" to INTEGER,
            "totalExperience" to INTEGER,

            "reckless" to INTEGER,
            "maxReckless" to INTEGER,
            "conservative" to INTEGER,
            "maxConservative" to INTEGER,

            "wounds" to INTEGER,
            "maxWounds" to INTEGER,
            "corruption" to INTEGER,
            "maxCorruption" to INTEGER,
            "stress" to INTEGER,
            "exhaustion" to INTEGER,
            // endregion

            // region INVENTORY
            "brass" to INTEGER,
            "silver" to INTEGER,
            "gold" to INTEGER,
            // endregion

            "skills" to TEXT,
            "talents" to TEXT)
}