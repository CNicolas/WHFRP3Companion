package com.nicolas.whfrp3database.tables.parsers

import com.beust.klaxon.Klaxon
import com.nicolas.whfrp3database.entities.player.CharacteristicValue
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Race
import org.jetbrains.anko.db.RowParser

class PlayerParser : RowParser<Player> {
    override fun parseRow(columns: Array<Any?>): Player =
            Player(id = columns[0].toInt(),
                    name = columns[1] as String,
                    race = Race.valueOf(columns[2] as String),
                    age = columns[3].toNullableInt(),
                    size = columns[4].toNullableInt(),

                    strength = CharacteristicValue(columns[5].toInt(), columns[6].toInt()),
                    toughness = CharacteristicValue(columns[7].toInt(), columns[8].toInt()),
                    agility = CharacteristicValue(columns[9].toInt(), columns[10].toInt()),
                    intelligence = CharacteristicValue(columns[11].toInt(), columns[12].toInt()),
                    willpower = CharacteristicValue(columns[13].toInt(), columns[14].toInt()),
                    fellowship = CharacteristicValue(columns[15].toInt(), columns[16].toInt()),

                    careerName = columns[17] as String?,
                    rank = columns[18].toInt(),
                    availableExperience = columns[19].toInt(),
                    totalExperience = columns[20].toInt(),
                    reckless = columns[21].toInt(),
                    maxReckless = columns[22].toInt(),
                    conservative = columns[23].toInt(),
                    maxConservative = columns[24].toInt(),
                    wounds = columns[25].toInt(),
                    maxWounds = columns[26].toInt(),
                    corruption = columns[27].toInt(),
                    maxCorruption = columns[28].toInt(),
                    stress = columns[29].toInt(),
                    exhaustion = columns[30].toInt(),
                    brass = columns[31].toInt(),
                    silver = columns[32].toInt(),
                    gold = columns[33].toInt(),
                    skills = Klaxon().parseArray(columns[34] as String)!!,
                    talents = Klaxon().parseArray(columns[35] as String)!!
            )
}

fun Player.toColumns(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "race" to race.toString(),
        "age" to age,
        "size" to size,

        // region CHARACTERISTICS
        "strength" to strength.value,
        "strengthFortune" to strength.fortuneValue,
        "toughness" to toughness.value,
        "toughnessFortune" to toughness.fortuneValue,
        "agility" to agility.value,
        "agilityFortune" to agility.fortuneValue,
        "intelligence" to intelligence.value,
        "intelligenceFortune" to intelligence.fortuneValue,
        "willpower" to willpower.value,
        "willpowerFortune" to willpower.fortuneValue,
        "fellowship" to fellowship.value,
        "fellowshipFortune" to fellowship.fortuneValue,
        // endregion

        // region STATE
        "wounds" to wounds,
        "maxWounds" to maxWounds,
        "corruption" to corruption,
        "maxCorruption" to maxCorruption,
        "stress" to stress,
        "exhaustion" to exhaustion,
        "careerName" to careerName,
        "rank" to rank,
        "availableExperience" to availableExperience,
        "totalExperience" to totalExperience,
        "reckless" to reckless,
        "maxReckless" to maxReckless,
        "conservative" to conservative,
        "maxConservative" to maxConservative,
        // endregion

        // region INVENTORY
        "brass" to brass,
        "silver" to silver,
        "gold" to gold,
        // endregion

        "skills" to Klaxon().toJsonString(skills),
        "talents" to Klaxon().toJsonString(talents)
)