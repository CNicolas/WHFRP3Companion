package com.nicolas.whfrp3database.staticData

import android.content.Context
import com.beust.klaxon.Klaxon
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.Talent
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentType
import com.nicolas.whfrp3database.extensions.talents.findByType

class TalentsLoader(context: Context) {
    val talents = loadTalents(context) ?: listOf()

    val passiveTalents = talents.filter { it.cooldown == TalentCooldown.PASSIVE }
    val exhaustibleTalents = talents.filter { it.cooldown == TalentCooldown.TALENT }

    fun getTalentsByType(talentType: TalentType): List<Talent> = talents.findByType(talentType)

    private fun loadTalents(context: Context): List<Talent>? {
        return Klaxon().parseArray(context.assets.open("talents.json"))
    }
}