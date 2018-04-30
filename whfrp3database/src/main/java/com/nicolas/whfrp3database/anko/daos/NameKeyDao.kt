package com.nicolas.whfrp3database.anko.daos

import com.nicolas.models.NamedEntity

internal interface NameKeyDao<E : NamedEntity> : Dao<E> {
    fun add(entity: E): E?

    fun findByName(name: String): E?
    fun findAll(): List<E>

    fun update(entity: E): E?

    fun delete(entity: E): Int
    fun deleteByName(name: String): Int
}