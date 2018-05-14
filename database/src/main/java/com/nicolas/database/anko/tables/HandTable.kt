package com.nicolas.database.anko.tables

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

internal const val HAND_TABLE_NAME = "Hand"

internal fun createHandTable(db: SQLiteDatabase) {
    db.createTable(HAND_TABLE_NAME, true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE,
            "name" to TEXT,

            "characteristicDicesCount" to INTEGER,
            "expertiseDicesCount" to INTEGER,
            "fortuneDicesCount" to INTEGER,
            "conservativeDicesCount" to INTEGER,
            "recklessDicesCount" to INTEGER,
            "challengeDicesCount" to INTEGER,
            "misfortuneDicesCount" to INTEGER)
}