package com.nicolas.models.action.effect

enum class Target {
    TARGET,
    PLAYER,
    ALLY,
    NONE
}

fun Target?.or(other: Target?): Target? {
    return when {
        other == null -> this
        this == null -> other
        else -> this // TODO : What happens if the other target is willingly different ?
    }
}