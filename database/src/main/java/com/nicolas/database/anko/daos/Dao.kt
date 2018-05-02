package com.nicolas.database.anko.daos

internal interface Dao<out E> {
    fun findById(id: Int): E?

    fun deleteAll(): Int
}