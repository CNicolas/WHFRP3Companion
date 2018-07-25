package com.nicolas.models

operator fun Int?.plus(other: Int?): Int? {
    return when {
        other == null -> this
        this == null -> other
        else -> this + other
    }
}

fun Boolean?.or(other: Boolean?): Boolean? {
    return when {
        other == null -> this
        this == null -> other
        else -> this || other
    }
}

operator fun <T> List<T>?.plus(other: List<T>?): List<T>? {
    return when {
        other == null -> this
        this == null -> other
        else -> this + other
    }
}