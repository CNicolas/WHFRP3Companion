package com.nicolas.database

import com.nicolas.models.hand.Hand

interface HandRepository {
    fun save(hand: Hand): Hand

    fun add(hand: Hand): Hand

    fun find(name: String): Hand?
    fun findAll(): List<Hand>

    fun update(hand: Hand): Hand

    fun delete(name: String)
    fun delete(hand: Hand)
}