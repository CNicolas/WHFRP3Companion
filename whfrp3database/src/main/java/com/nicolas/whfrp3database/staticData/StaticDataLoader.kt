package com.nicolas.whfrp3database.staticData

import android.content.Context
import com.beust.klaxon.Klaxon
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.Talent

internal fun loadSkills(context: Context): List<Skill>? {
    return Klaxon().parseArray(context.assets.open("skills.json"))
}

internal fun loadTalents(context: Context): List<Talent>? {
    return Klaxon().parseArray(context.assets.open("talents.json"))
}