package com.nicolas.whfrp3database.daos

import com.nicolas.whfrp3database.entities.NamedEntity

interface NameKeyDao<E : NamedEntity> : Dao<E> {
    fun add(entity: E): E?

    fun findByName(name: String): E?
    fun findAll(): List<E>

    fun update(entity: E): E?

    fun delete(entity: E): Int
    fun deleteByName(name: String): Int
}