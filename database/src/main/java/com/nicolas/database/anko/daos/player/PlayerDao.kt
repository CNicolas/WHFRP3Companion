package com.nicolas.database.anko.daos.player

import android.database.Cursor
import com.nicolas.database.anko.DatabaseOpenHelper
import com.nicolas.database.anko.daos.AbstractNameKeyDao
import com.nicolas.database.anko.tables.PLAYER_TABLE_NAME
import com.nicolas.database.anko.tables.parsers.PlayerParser
import com.nicolas.database.anko.tables.parsers.toColumns
import com.nicolas.models.player.Player
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.parseOpt

internal class PlayerDao(databaseHelper: DatabaseOpenHelper) : AbstractNameKeyDao<Player>(databaseHelper) {
    override val tableName = PLAYER_TABLE_NAME

    override fun parse(cursor: Cursor): Player? = cursor.parseOpt(PlayerParser())

    override fun parseAll(cursor: Cursor): List<Player> = cursor.parseList(PlayerParser())

    override fun getColumns(entity: Player): Map<String, Any?> = entity.toColumns()
}