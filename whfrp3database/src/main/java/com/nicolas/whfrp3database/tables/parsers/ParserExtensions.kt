package com.nicolas.whfrp3database.tables.parsers

internal fun Any?.toInt(): Int = (this as Long).toInt()
internal fun Any?.toNullableInt(): Int? = (this as Long?)?.toInt()