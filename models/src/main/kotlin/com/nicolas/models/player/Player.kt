package com.nicolas.models.player

import com.nicolas.models.NamedEntity
import com.nicolas.models.effect.Effect
import com.nicolas.models.extensions.getEquippedArmors
import com.nicolas.models.item.Item
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.enums.Characteristic.*
import com.nicolas.models.player.enums.Race
import com.nicolas.models.skill.Skill
import com.nicolas.models.talent.Talent
import java.io.Serializable

data class Player(override var name: String,

                  var race: Race = Race.HUMAN,
                  var age: Int? = null,
                  var size: Int? = null,
                  var description: String? = null,

                  var strength: CharacteristicValue = CharacteristicValue(0),
                  var toughness: CharacteristicValue = CharacteristicValue(0),
                  var agility: CharacteristicValue = CharacteristicValue(0),
                  var intelligence: CharacteristicValue = CharacteristicValue(0),
                  var willpower: CharacteristicValue = CharacteristicValue(0),
                  var fellowship: CharacteristicValue = CharacteristicValue(0),

                  var careerName: String? = null,
                  var rank: Int = 0,
                  var experience: Int = 0,
                  var maxExperience: Int = 0,

                  var maxConservative: Int = 0,
                  var maxReckless: Int = 0,
                  var stance: Int = 0,

                  var wounds: Int = 0,
                  var maxWounds: Int = 0,
                  var corruption: Int = 0,
                  var maxCorruption: Int = 0,
                  var stress: Int = 0,
                  var exhaustion: Int = 0,

                  var brass: Int = 0,
                  var silver: Int = 0,
                  var gold: Int = 0,

                  var items: List<Item> = listOf(),
                  var skills: List<Skill> = listOf(),
                  var talents: List<Talent> = listOf(),
                  var effects: List<Effect> = listOf(),

                  override val id: Int = -1) : NamedEntity, Serializable {

    val maxStress: Int
        get() = willpower.value * 2

    val maxExhaustion: Int
        get() = toughness.value * 2

    val encumbrance: Int
        get() = items.sumBy { it.encumbrance }

    val encumbranceOverload: Int
        get() = strength.value * 5 + strength.fortuneValue + when (race) {
            Race.DWARF -> 5
            else -> 0
        }

    val maxEncumbrance: Int
        get() = encumbranceOverload + 5

    val defense: Int
        get() = getEquippedArmors().sumBy { it.defense } + effects.sumBy { it.defense ?: 0 }

    val soak: Int
        get() = getEquippedArmors().sumBy { it.soak } + effects.sumBy { it.soak ?: 0 }

    operator fun get(characteristic: Characteristic): CharacteristicValue = when (characteristic) {
        STRENGTH -> strength
        TOUGHNESS -> toughness
        AGILITY -> agility
        INTELLIGENCE -> intelligence
        WILLPOWER -> willpower
        FELLOWSHIP -> fellowship
    }
}