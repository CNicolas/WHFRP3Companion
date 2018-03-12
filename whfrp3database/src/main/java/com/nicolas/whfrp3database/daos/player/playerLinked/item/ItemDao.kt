package com.nicolas.whfrp3database.daos.player.playerLinked.item

import android.database.Cursor
import com.nicolas.whfrp3database.DatabaseOpenHelper
import com.nicolas.whfrp3database.daos.player.playerLinked.AbstractPlayerLinkedDao
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.Item
import com.nicolas.whfrp3database.tables.ITEM_TABLE_NAME
import com.nicolas.whfrp3database.tables.parsers.ItemParser
import com.nicolas.whfrp3database.tables.parsers.toColumns
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.parseOpt

class ItemDao(databaseHelper: DatabaseOpenHelper) : AbstractPlayerLinkedDao<Item>(databaseHelper) {
    override val tableName = ITEM_TABLE_NAME

    override fun parse(cursor: Cursor): Item? = cursor.parseOpt(ItemParser())

    override fun parseAll(cursor: Cursor): List<Item> = cursor.parseList(ItemParser())

    override fun getColumns(entity: Item, player: Player): Map<String, Any?> = entity.toColumns(player)
}