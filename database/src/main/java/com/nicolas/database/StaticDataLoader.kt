package com.nicolas.database

import android.content.Context
import com.google.gson.Gson
import com.nicolas.models.action.Action
import com.nicolas.models.effect.Effect
import com.nicolas.models.skill.Skill
import com.nicolas.models.talent.Talent

fun loadSkills(context: Context): List<Skill> {
    val skillsReader = context.assets.open("skills.json").reader()

    return Gson().fromJson(skillsReader, genericType<List<Skill>>()) ?: listOf()
}

fun loadTalents(context: Context): List<Talent> {
    val talentsReader = context.assets.open("talents.json").reader()

    return Gson().fromJson(talentsReader, genericType<List<Talent>>()) ?: listOf()
}

fun loadEffects(context: Context): List<Effect> {
    val effectsReader = context.assets.open("effects.json").reader()

    return Gson().fromJson(effectsReader, genericType<List<Effect>>()) ?: listOf()
}

fun loadActions(context: Context): List<Action> {
    val actionsReader = context.assets.open("actions.json").reader()

    return Gson().fromJson(actionsReader, genericType<List<Action>>()) ?: listOf()
}
