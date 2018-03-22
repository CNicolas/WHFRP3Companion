package com.nicolas.whfrp3database.tables.parsers

import com.google.gson.Gson
import com.nicolas.whfrp3database.entities.player.CharacteristicValue
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Race
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.Talent
import org.jetbrains.anko.db.RowParser

internal class PlayerParser : RowParser<Player> {
    override fun parseRow(columns: Array<Any?>): Player =
            Player(id = columns[0].toInt(),
                    name = columns[1] as String,
                    race = Race.valueOf(columns[2] as String),
                    age = columns[3].toNullableInt(),
                    size = columns[4].toNullableInt(),
                    description = columns[5] as String?,

                    strength = CharacteristicValue(columns[6].toInt(), columns[7].toInt()),
                    toughness = CharacteristicValue(columns[8].toInt(), columns[9].toInt()),
                    agility = CharacteristicValue(columns[10].toInt(), columns[11].toInt()),
                    intelligence = CharacteristicValue(columns[12].toInt(), columns[13].toInt()),
                    willpower = CharacteristicValue(columns[14].toInt(), columns[15].toInt()),
                    fellowship = CharacteristicValue(columns[16].toInt(), columns[17].toInt()),

                    careerName = columns[18] as String?,
                    rank = columns[19].toInt(),
                    experience = columns[20].toInt(),
                    maxExperience = columns[21].toInt(),
                    reckless = columns[22].toInt(),
                    maxReckless = columns[23].toInt(),
                    conservative = columns[24].toInt(),
                    maxConservative = columns[25].toInt(),
                    wounds = columns[26].toInt(),
                    maxWounds = columns[27].toInt(),
                    corruption = columns[28].toInt(),
                    maxCorruption = columns[29].toInt(),
                    stress = columns[30].toInt(),
                    exhaustion = columns[31].toInt(),
                    brass = columns[32].toInt(),
                    silver = columns[33].toInt(),
                    gold = columns[34].toInt(),
                    skills = Gson().fromJson(columns[35] as String, genericType<List<Skill>>()),
                    talents = Gson().fromJson(columns[36] as String, genericType<List<Talent>>())
            )
}

fun Player.toColumns(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "race" to race.toString(),
        "age" to age,
        "size" to size,
        "description" to description,

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
        "experience" to experience,
        "maxExperience" to maxExperience,
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

        "skills" to Gson().toJson(skills),
        "talents" to Gson().toJson(talents)
)