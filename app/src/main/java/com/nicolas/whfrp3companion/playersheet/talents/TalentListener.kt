package com.nicolas.whfrp3companion.playersheet.talents

import com.nicolas.models.player.talent.Talent

interface TalentListener {
    fun onAddTalent(talent: Talent)
    fun onToggleTalentEquipment(talent: Talent)
    fun onRemoveTalent(talent: Talent)
}
