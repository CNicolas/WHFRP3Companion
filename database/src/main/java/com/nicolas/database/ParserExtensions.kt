package com.nicolas.database

import com.google.gson.reflect.TypeToken

internal fun Any?.toInt(): Int = (this as Long).toInt()
internal fun Any?.toNullableInt(): Int? = (this as Long?)?.toInt()

internal fun Map<String, Any?>.toPairs(): Array<Pair<String, Any?>> {
    val list = arrayListOf<Pair<String, Any?>>()

    forEach {
        list.add(Pair(it.key, it.value))
    }

    return list.toArray(arrayOf<Pair<String, Any?>>())
}

internal inline fun <reified T> genericType() = object: TypeToken<T>() {}.type