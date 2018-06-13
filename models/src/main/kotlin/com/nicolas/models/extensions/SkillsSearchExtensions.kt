package com.nicolas.models.extensions

import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.skill.Skill

fun List<Skill>.findSkills(text: String? = null, characteristic: Characteristic? = null): List<Skill> {
    var filteredSkills = toList()

    if (characteristic != null) {
        filteredSkills = findByCharacteristic(characteristic)
    }

    if (text != null) {
        filteredSkills = filteredSkills.findByText(text)
    }

    return filteredSkills
}

fun List<Skill>.findByCharacteristic(characteristic: Characteristic) =
        filter { it.characteristic == characteristic }

fun List<Skill>.findByText(text: String) =
        filter { it.name.contains(text, true) }