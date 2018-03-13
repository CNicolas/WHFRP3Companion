package com.nicolas.whfrp3database.tables

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

internal const val ITEM_TABLE_NAME = "Item"

internal fun createItemTable(db: SQLiteDatabase) {
    db.createTable(ITEM_TABLE_NAME, true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE,
            "playerId" to INTEGER,

            "type" to TEXT,
            "name" to TEXT,
            "description" to TEXT,
            "encumbrance" to INTEGER,
            "quantity" to INTEGER,
            "quality" to TEXT,
            "subType" to TEXT,

            "uses" to INTEGER,

            "isEquipped" to INTEGER,

            "soak" to INTEGER,
            "defense" to INTEGER,

            "damage" to INTEGER,
            "criticalLevel" to INTEGER,
            "range" to TEXT,

            FOREIGN_KEY("playerId", PLAYER_TABLE_NAME, "id")
    )
}