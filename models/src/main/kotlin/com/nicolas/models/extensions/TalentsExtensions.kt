package com.nicolas.models.extensions

import com.nicolas.models.player.Player
import com.nicolas.models.player.talent.Talent
import com.nicolas.models.player.talent.TalentCooldown.PASSIVE
import com.nicolas.models.player.talent.TalentCooldown.TALENT
import com.nicolas.models.player.talent.TalentType

fun Player.addTalent(talent: Talent): Player {
    val mutableTalents = talents.toMutableList()

    val alreadyExistingTalent = mutableTalents.find { talent.name == name }
    if (alreadyExistingTalent == null) {
        mutableTalents.add(talent)
    }

    talents = mutableTalents.toList()

    return this
}

fun Player.removeTalent(talent: Talent): Player {
    val mutableTalents = talents.toMutableList()
    mutableTalents.remove(talent)
    talents = mutableTalents.toList()

    return this
}

fun Player.getTalentsByType(talentType: TalentType) = talents.findByType(talentType)
fun Player.getPassiveTalents() = talents.filter { it.cooldown == PASSIVE }
fun Player.getExhaustibleTalents() = talents.filter { it.cooldown == TALENT }
fun Player.getEquippedTalents() = talents.filter { it.isEquipped }

fun Player.equipTalent(talent: Talent): List<Talent> {
    talents.firstOrNull { it == talent }?.isEquipped = true

    return getEquippedTalents()
}

fun Player.toggleEquipment(talent: Talent): Player {
    talents.firstOrNull { it == talent }?.let {
        it.isEquipped = !it.isEquipped
    }

    return this
}

val List<Talent>.passive: List<Talent>
    get() = filter { it.cooldown == PASSIVE }
val List<Talent>.exhaustible: List<Talent>
    get() = filter { it.cooldown == TALENT }

fun List<Talent>.findByType(talentType: TalentType) =
        filter { it.type == talentType }
