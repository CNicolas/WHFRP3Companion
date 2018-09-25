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
    return loadMeleeActions(context) +
            loadRangeActions(context) +
            loadSpellActions(context) +
            loadBlessingActions(context) +
            loadSupportActions(context)
}

fun loadMeleeActions(context: Context): List<Action> {
    val meleeActionsReader = context.assets.open("actions_melee.json").reader()

    return Gson().fromJson(meleeActionsReader, genericType<List<Action>>()) ?: listOf()
}

fun loadRangeActions(context: Context): List<Action> {
    val rangeActionsReader = context.assets.open("actions_range.json").reader()

    return Gson().fromJson(rangeActionsReader, genericType<List<Action>>()) ?: listOf()
}

fun loadSpellActions(context: Context): List<Action> {
    val spellActionsReader = context.assets.open("actions_spell.json").reader()

    return Gson().fromJson(spellActionsReader, genericType<List<Action>>()) ?: listOf()
}

fun loadBlessingActions(context: Context): List<Action> {
    val blessingActionsReader = context.assets.open("actions_blessing.json").reader()

    return Gson().fromJson(blessingActionsReader, genericType<List<Action>>()) ?: listOf()
}

fun loadSupportActions(context: Context): List<Action> {
    val supportActionsReader = context.assets.open("actions_support.json").reader()

    return Gson().fromJson(supportActionsReader, genericType<List<Action>>()) ?: listOf()
}
