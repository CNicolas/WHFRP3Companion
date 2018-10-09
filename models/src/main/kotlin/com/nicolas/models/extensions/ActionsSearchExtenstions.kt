package com.nicolas.models.extensions

import com.nicolas.models.action.*


fun List<Action>.search(actionSearch: ActionSearch): List<Action> {
    var filteredActions = toList()

    actionSearch.actionType?.let {
        filteredActions = filteredActions.findByActionType(it)
    }

    filteredActions = actionSearch.traits.flatMap { filteredActions.findByTrait(it) }

    actionSearch.cooldown?.let {
        filteredActions = filteredActions.findByCooldown(it)
    }

    actionSearch.text.let {
        filteredActions = filteredActions.findByText(it)
    }

    return filteredActions
}

fun List<Action>.findByTrait(trait: Trait): List<Action> = filter { it.traits.contains(trait) }

fun List<Action>.findByActionType(actionType: ActionType): List<Action> = filter { it.type == actionType }

fun List<Action>.findByCooldown(cooldown: Int): List<Action> =
        filter { it.conservativeSide.cooldown == cooldown || it.recklessSide.cooldown == cooldown }

fun List<Action>.findByText(text: String): List<Action> {
    if (text.isEmpty()) {
        return this
    }

    return filter {
        it.name.contains(text, true) ||
                it.conditionsText?.contains(text, true) ?: false ||
                it.skillsString?.contains(text, true) ?: false ||
                it.conditionsString?.contains(text, true) ?: false ||
                it.conservativeSide.filterByText(text) ||
                it.recklessSide.filterByText(text)
    }
}

fun ActionSide.filterByText(text: String): Boolean {
    if (text.isEmpty()) {
        return true
    }

    return specialText?.contains(text, true) ?: false
}