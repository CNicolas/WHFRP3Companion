package com.nicolas.database.actions

import com.google.gson.Gson
import com.nicolas.database.genericType
import com.nicolas.models.action.Action

class ActionsFilesLoader {
    fun loadActions(): List<Action> {
        return loadMeleeActions() +
                loadRangeActions() +
                loadSpellActions() +
                loadSummoningActions() +
                loadSupportActions()
    }

    fun loadMeleeActions(): List<Action> {
        val actionsReader = javaClass.classLoader.getResourceAsStream("actions_melee.json").reader()

        return Gson().fromJson(actionsReader, genericType<List<Action>>()) ?: listOf()
    }

    fun loadRangeActions(): List<Action> {
        val actionsReader = javaClass.classLoader.getResourceAsStream("actions_range.json").reader()

        return Gson().fromJson(actionsReader, genericType<List<Action>>()) ?: listOf()
    }

    fun loadSpellActions(): List<Action> {
        val actionsReader = javaClass.classLoader.getResourceAsStream("actions_spell.json").reader()

        return Gson().fromJson(actionsReader, genericType<List<Action>>()) ?: listOf()
    }

    fun loadSummoningActions(): List<Action> {
        val actionsReader = javaClass.classLoader.getResourceAsStream("actions_summoning.json").reader()

        return Gson().fromJson(actionsReader, genericType<List<Action>>()) ?: listOf()
    }

    fun loadSupportActions(): List<Action> {
        val actionsReader = javaClass.classLoader.getResourceAsStream("actions_support.json").reader()

        return Gson().fromJson(actionsReader, genericType<List<Action>>()) ?: listOf()
    }
}