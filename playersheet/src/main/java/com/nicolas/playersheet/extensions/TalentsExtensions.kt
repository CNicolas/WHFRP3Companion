package com.nicolas.playersheet.extensions

import com.nicolas.models.extensions.findByType
import com.nicolas.models.player.playerLinked.talent.Talent
import com.nicolas.models.player.playerLinked.talent.TalentCooldown
import com.nicolas.models.player.playerLinked.talent.TalentCooldown.PASSIVE
import com.nicolas.models.player.playerLinked.talent.TalentCooldown.TALENT
import com.nicolas.models.player.playerLinked.talent.TalentType

fun List<Talent>.findTalents(text: String? = null,
                             cooldown: TalentCooldown? = null,
                             type: TalentType? = null): List<Talent> {
    var filteredTalents = toList()

    if (cooldown != null) {
        filteredTalents = findByCooldown(cooldown)
    }

    if (type != null) {
        filteredTalents = findByType(type)
    }

    if (text != null) {
        filteredTalents = filteredTalents.findByText(text)
    }

    return filteredTalents
}

fun List<Talent>.findByCooldown(talentCooldown: TalentCooldown) = when (talentCooldown) {
    PASSIVE -> filter { it.cooldown == PASSIVE }
    TALENT -> filter { it.cooldown == TALENT }
}

fun List<Talent>.findByText(text: String) =
        filter { it.name.contains(text, true) || it.description.contains(text, true) }
