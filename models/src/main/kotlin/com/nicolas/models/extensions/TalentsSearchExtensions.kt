package com.nicolas.models.extensions

import com.nicolas.models.talent.Talent
import com.nicolas.models.talent.TalentCooldown
import com.nicolas.models.talent.TalentType

fun List<Talent>.findTalents(text: String? = null,
                             cooldown: TalentCooldown? = null,
                             type: TalentType? = null): List<Talent> {
    var filteredTalents = toList()

    if (type != null) {
        filteredTalents = filteredTalents.findByType(type)
    }

    if (cooldown != null) {
        filteredTalents = filteredTalents.findByCooldown(cooldown)
    }

    if (text != null) {
        filteredTalents = filteredTalents.findByText(text)
    }

    return filteredTalents
}

fun List<Talent>.findByCooldown(talentCooldown: TalentCooldown) = when (talentCooldown) {
    TalentCooldown.PASSIVE -> filter { it.cooldown == TalentCooldown.PASSIVE }
    TalentCooldown.TALENT -> filter { it.cooldown == TalentCooldown.TALENT }
    TalentCooldown.SESSION -> filter { it.cooldown == TalentCooldown.SESSION }
}

fun List<Talent>.findByText(text: String) =
        filter { it.name.contains(text, true) || it.description.contains(text, true) }