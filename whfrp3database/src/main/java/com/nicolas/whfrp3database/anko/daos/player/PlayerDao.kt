package com.nicolas.whfrp3database.anko.daos.player

import android.database.Cursor
import com.nicolas.models.player.Player
import com.nicolas.whfrp3database.anko.DatabaseOpenHelper
import com.nicolas.whfrp3database.anko.daos.AbstractNameKeyDao
import com.nicolas.whfrp3database.anko.tables.PLAYER_TABLE_NAME
import com.nicolas.whfrp3database.anko.tables.parsers.PlayerParser
import com.nicolas.whfrp3database.anko.tables.parsers.toColumns
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.parseOpt

internal class PlayerDao(databaseHelper: DatabaseOpenHelper) : AbstractNameKeyDao<Player>(databaseHelper) {
    override val tableName = PLAYER_TABLE_NAME

    override fun parse(cursor: Cursor): Player? = cursor.parseOpt(PlayerParser())

    override fun parseAll(cursor: Cursor): List<Player> = cursor.parseList(PlayerParser())

    override fun getColumns(entity: Player): Map<String, Any?> = entity.toColumns()
}