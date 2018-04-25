package com.nicolas.whfrp3database.staticData

import android.content.Context
import com.google.gson.Gson
import com.nicolas.models.player.playerLinked.skill.Skill
import com.nicolas.models.player.playerLinked.talent.Talent
import com.nicolas.whfrp3database.tables.parsers.genericType

fun loadSkills(context: Context): List<Skill> {
    val skillsReader = context.assets.open("skills.json").reader()

    return Gson().fromJson(skillsReader, genericType<List<Skill>>()) ?: listOf()
}

fun loadTalents(context: Context): List<Talent> {
    val talentsReader = context.assets.open("talents.json").reader()

    return Gson().fromJson(talentsReader, genericType<List<Talent>>()) ?: listOf()
}