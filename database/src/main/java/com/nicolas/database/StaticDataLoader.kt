package com.nicolas.database

import android.content.Context
import com.google.gson.Gson
import com.nicolas.models.effect.Effect
import com.nicolas.models.player.talent.Talent
import com.nicolas.models.skill.Skill

fun loadSkills(context: Context): List<Skill> {
    val skillsReader = context.assets.open("skills.json").reader()

    return Gson().fromJson(skillsReader, genericType<List<Skill>>()) ?: listOf()
}

fun loadTalents(context: Context): List<Talent> {
    val talentsReader = context.assets.open("talents.json").reader()

    return Gson().fromJson(talentsReader, genericType<List<Talent>>()) ?: listOf()
}

fun loadEffects(context: Context): List<Effect> {
    val talentsReader = context.assets.open("effects.json").reader()

    return Gson().fromJson(talentsReader, genericType<List<Effect>>()) ?: listOf()
}
