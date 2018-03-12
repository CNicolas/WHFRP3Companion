package com.nicolas.whfrp3database.tables.parsers

internal fun Any?.toInt(): Int = (this as Long).toInt()
internal fun Any?.toNullableInt(): Int? = (this as Long?)?.toInt()

fun Map<String, Any?>.toPairs(): Array<Pair<String, Any?>> {
    val list = arrayListOf<Pair<String, Any?>>()

    forEach {
        list.add(Pair(it.key, it.value))
    }

    return list.toArray(arrayOf<Pair<String, Any?>>())
}