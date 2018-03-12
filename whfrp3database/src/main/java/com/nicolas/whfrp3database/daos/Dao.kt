package com.nicolas.whfrp3database.daos

interface Dao<out E> {
    fun findById(id: Int): E?

    fun deleteAll(): Int
}