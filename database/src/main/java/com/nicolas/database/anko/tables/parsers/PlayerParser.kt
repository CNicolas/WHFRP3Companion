package com.nicolas.database.anko.tables.parsers

import com.google.gson.GsonBuilder
import com.nicolas.database.genericType
import com.nicolas.database.toInt
import com.nicolas.database.toNullableInt
import com.nicolas.models.player.CharacteristicValue
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Race
import com.nicolas.models.player.item.Item
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.talent.Talent
import org.jetbrains.anko.db.RowParser

internal class PlayerParser : RowParser<Player> {
    private val gson = GsonBuilder()
            .registerTypeAdapter(Item::class.java, ItemDeserializer())
            .create()

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
                    maxConservative = columns[22].toInt(),
                    maxReckless = columns[23].toInt(),
                    stance = columns[24].toInt(),
                    wounds = columns[25].toInt(),
                    maxWounds = columns[26].toInt(),
                    corruption = columns[27].toInt(),
                    maxCorruption = columns[28].toInt(),
                    stress = columns[29].toInt(),
                    exhaustion = columns[30].toInt(),
                    brass = columns[31].toInt(),
                    silver = columns[32].toInt(),
                    gold = columns[33].toInt(),
                    items = gson.fromJson(columns[34] as String, genericType<List<Item>>()),
                    skills = gson.fromJson(columns[35] as String, genericType<List<Skill>>()),
                    talents = gson.fromJson(columns[36] as String, genericType<List<Talent>>())
            )
}

fun Player.toColumns(): Map<String, Any?> {
    val gson = GsonBuilder()
            .registerTypeAdapter(Item::class.java, ItemDeserializer())
            .create()

    return mapOf(
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
            "maxConservative" to maxConservative,
            "maxReckless" to maxReckless,
            "stance" to stance,
            // endregion

            // region INVENTORY
            "brass" to brass,
            "silver" to silver,
            "gold" to gold,
            // endregion

            "items" to gson.toJson(items),
            "skills" to gson.toJson(skills),
            "talents" to gson.toJson(talents)
    )
}