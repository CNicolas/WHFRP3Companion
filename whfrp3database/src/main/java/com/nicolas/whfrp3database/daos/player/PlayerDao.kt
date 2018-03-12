package com.nicolas.whfrp3database.daos.player

import android.database.Cursor
import com.nicolas.whfrp3database.DatabaseOpenHelper
import com.nicolas.whfrp3database.daos.AbstractNameKeyDao
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.tables.PLAYER_TABLE_NAME
import com.nicolas.whfrp3database.tables.parsers.PlayerParser
import com.nicolas.whfrp3database.tables.parsers.toColumns
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.parseOpt

class PlayerDao(databaseHelper: DatabaseOpenHelper) : AbstractNameKeyDao<Player>(databaseHelper) {
    override val tableName = PLAYER_TABLE_NAME

    override fun parse(cursor: Cursor): Player? = cursor.parseOpt(PlayerParser())

    override fun parseAll(cursor: Cursor): List<Player> = cursor.parseList(PlayerParser())

    override fun getColumns(entity: Player): Map<String, Any?> = entity.toColumns()
}