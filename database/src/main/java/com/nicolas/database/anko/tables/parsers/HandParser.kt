package com.nicolas.database.anko.tables.parsers

import com.nicolas.database.toInt
import com.nicolas.models.hand.Hand
import org.jetbrains.anko.db.RowParser

internal class HandParser : RowParser<Hand> {
    override fun parseRow(columns: Array<Any?>): Hand =
            Hand(id = columns[0].toInt(),
                    name = columns[1] as String,
                    characteristicDicesCount = columns[2].toInt(),
                    expertiseDicesCount = columns[3].toInt(),
                    fortuneDicesCount = columns[4].toInt(),
                    conservativeDicesCount = columns[5].toInt(),
                    recklessDicesCount = columns[6].toInt(),
                    challengeDicesCount = columns[7].toInt(),
                    misfortuneDicesCount = columns[8].toInt()
            )
}

internal fun Hand.toColumns(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "characteristicDicesCount" to characteristicDicesCount,
        "expertiseDicesCount" to expertiseDicesCount,
        "fortuneDicesCount" to fortuneDicesCount,
        "conservativeDicesCount" to conservativeDicesCount,
        "recklessDicesCount" to recklessDicesCount,
        "challengeDicesCount" to challengeDicesCount,
        "misfortuneDicesCount" to misfortuneDicesCount
)