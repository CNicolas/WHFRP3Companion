package com.nicolas.whfrp3database.tables

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

const val HAND_TABLE_NAME = "Hand"

fun createHandTable(db: SQLiteDatabase) {
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