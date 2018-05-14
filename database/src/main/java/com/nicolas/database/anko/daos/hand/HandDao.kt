package com.nicolas.database.anko.daos.hand

import android.database.Cursor
import com.nicolas.database.anko.DatabaseOpenHelper
import com.nicolas.database.anko.daos.AbstractNameKeyDao
import com.nicolas.database.anko.tables.HAND_TABLE_NAME
import com.nicolas.database.anko.tables.parsers.HandParser
import com.nicolas.database.anko.tables.parsers.toColumns
import com.nicolas.models.hand.Hand
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.parseOpt

internal class HandDao(databaseHelper: DatabaseOpenHelper) : AbstractNameKeyDao<Hand>(databaseHelper) {
    override val tableName = HAND_TABLE_NAME

    override fun parse(cursor: Cursor): Hand? = cursor.parseOpt(HandParser())

    override fun parseAll(cursor: Cursor): List<Hand> = cursor.parseList(HandParser())

    override fun getColumns(entity: Hand): Map<String, Any?> = entity.toColumns()
}