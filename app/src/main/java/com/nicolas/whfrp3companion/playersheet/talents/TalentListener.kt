package com.nicolas.whfrp3companion.playersheet.talents

import com.nicolas.models.player.playerLinked.talent.Talent

interface TalentListener {
    fun onAddTalent(talent: Talent)
    fun onToggleTalentEquipment(talent: Talent)
    fun onRemoveTalent(talent: Talent)
}

internal fun List<TalentListener>.notifyAddTalent(talent: Talent) = forEach { it.onAddTalent(talent) }
internal fun List<TalentListener>.notifyToggleTalentEquipment(talent: Talent) = forEach { it.onToggleTalentEquipment(talent) }
internal fun List<TalentListener>.notifyRemoveTalent(talent: Talent) = forEach { it.onRemoveTalent(talent) }
