package com.nicolas.whfrp3database.anko.daos

internal interface Dao<out E> {
    fun findById(id: Int): E?

    fun deleteAll(): Int
}