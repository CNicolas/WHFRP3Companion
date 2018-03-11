package com.nicolas.whfrp3database.tables

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.PRIMARY_KEY
import org.jetbrains.anko.db.UNIQUE
import org.jetbrains.anko.db.createTable

const val HAND_TABLE_NAME = "Hand"


fun createHandTable(db: SQLiteDatabase) {
    db.createTable(HAND_TABLE_NAME, true,
            "id" to INTEGER + PRIMARY_KEY + UNIQUE,

            "characteristicDicesCount" to INTEGER,
            "expertiseDicesCount" to INTEGER,
            "fortuneDicesCount" to INTEGER,
            "conservativeDicesCount" to INTEGER,
            "recklessDicesCount" to INTEGER,
            "challengeDicesCount" to INTEGER,
            "misfortuneDicesCount" to INTEGER)
}