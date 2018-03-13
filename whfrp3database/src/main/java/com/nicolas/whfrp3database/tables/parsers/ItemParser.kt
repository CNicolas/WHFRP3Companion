package com.nicolas.whfrp3database.tables.parsers

import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.item.*
import com.nicolas.whfrp3database.entities.player.playerLinked.item.enums.*
import org.jetbrains.anko.db.RowParser


class ItemParser : RowParser<Item> {
    override fun parseRow(columns: Array<Any?>): Item {
        return when (ItemType.valueOf(columns[2] as String)) {
            ItemType.ARMOR -> parseArmor(columns)
            ItemType.EXPANDABLE -> parseExpandable(columns)
            ItemType.GENERIC_ITEM -> parseGenericItem(columns)
            ItemType.WEAPON -> parseWeapon(columns)
        }
    }

    private fun parseArmor(columns: Array<Any?>): Armor =
            Armor(
                    id = columns[0].toInt(),
                    name = columns[3] as String,
                    description = columns[4] as String,
                    encumbrance = columns[5].toInt(),
                    quantity = columns[6].toInt(),
                    quality = Quality.valueOf(columns[7] as String),
                    subType = ArmorType.valueOf(columns[8] as String),
                    isEquipped = columns[10] as Boolean,
                    soak = columns[11].toInt(),
                    defense = columns[12].toInt()
            )

    private fun parseExpandable(columns: Array<Any?>): Expandable =
            Expandable(
                    id = columns[0].toInt(),
                    name = columns[3] as String,
                    description = columns[4] as String,
                    encumbrance = columns[5].toInt(),
                    quantity = columns[6].toInt(),
                    quality = Quality.valueOf(columns[7] as String),
                    uses = columns[9].toInt()
            )

    private fun parseGenericItem(columns: Array<Any?>): GenericItem =
            GenericItem(
                    id = columns[0].toInt(),
                    name = columns[3] as String,
                    description = columns[4] as String?,
                    encumbrance = columns[5].toInt(),
                    quantity = columns[6].toInt(),
                    quality = Quality.valueOf(columns[7] as String)
            )

    private fun parseWeapon(columns: Array<Any?>): Weapon =
            Weapon(
                    id = columns[0].toInt(),
                    name = columns[3] as String,
                    description = columns[4] as String,
                    encumbrance = columns[5].toInt(),
                    quantity = columns[6].toInt(),
                    quality = Quality.valueOf(columns[7] as String),
                    subType = WeaponType.valueOf(columns[8] as String),
                    isEquipped = columns[10] as Boolean,
                    damage = columns[13].toInt(),
                    criticalLevel = columns[14].toInt(),
                    range = Range.valueOf(columns[15] as String)
            )
}

fun Item.toColumns(player: Player): Map<String, Any?> = mapOf(
        "id" to id,
        "playerId" to player.id,

        "type" to type.toString(),
        "name" to name,
        "description" to description,
        "encumbrance" to encumbrance,
        "quantity" to quantity,
        "quality" to quality.toString(),

        "uses" to uses,

        "isEquipped" to isEquipped,
        "subType" to subType.toString(),

        "soak" to soak,
        "defense" to defense,

        "damage" to damage,
        "criticalLevel" to criticalLevel,
        "range" to range.toString()
)