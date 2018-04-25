package com.nicolas.models.extensions

import com.nicolas.models.player.Player
import com.nicolas.models.player.playerLinked.talent.Talent
import com.nicolas.models.player.playerLinked.talent.TalentCooldown.PASSIVE
import com.nicolas.models.player.playerLinked.talent.TalentCooldown.TALENT
import com.nicolas.models.player.playerLinked.talent.TalentType

fun Player.addTalent(talent: Talent): List<Talent> {
    val mutableTalents = talents.toMutableList()
    mutableTalents.add(talent)
    talents = mutableTalents.toList()

    return talents
}

fun Player.getTalentsByType(talentType: TalentType) = talents.findByType(talentType)
fun Player.getPassiveTalents() = talents.filter { it.cooldown == PASSIVE }
fun Player.getExhaustibleTalents() = talents.filter { it.cooldown == TALENT }
fun Player.getEquippedTalents() = talents.filter { it.isEquipped }

fun Player.equipTalent(talent: Talent): List<Talent> {
    talents.firstOrNull { it == talent }?.isEquipped = true

    return getEquippedTalents()
}


fun List<Talent>.findByType(talentType: TalentType) =
        filter { it.type == talentType }
