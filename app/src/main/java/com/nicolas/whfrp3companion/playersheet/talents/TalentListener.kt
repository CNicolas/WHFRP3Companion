package com.nicolas.whfrp3companion.playersheet.talents

import com.nicolas.models.talent.Talent

interface TalentListener {
    fun onAddTalent(talent: Talent)
    fun onToggleTalentEquipment(talent: Talent)
    fun onRemoveTalent(talent: Talent)
}
