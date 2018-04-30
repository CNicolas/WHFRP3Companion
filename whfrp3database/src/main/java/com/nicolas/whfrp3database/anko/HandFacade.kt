package com.nicolas.whfrp3database.anko

import android.content.Context
import com.nicolas.models.hand.Hand
import com.nicolas.whfrp3database.anko.daos.hand.HandDao

class HandFacade(context: Context) {
    private val handDao = HandDao(context.database)

    fun save(hand: Hand): Hand = if (find(hand.name) == null) {
        add(hand)
    } else {
        update(hand)
    }

    fun add(hand: Hand) = handDao.add(hand)!!

    fun find(name: String): Hand? = handDao.findByName(name)

    fun findAll(): List<Hand> = handDao.findAll()

    fun update(hand: Hand) = handDao.update(hand)!!

    fun delete(name: String) = handDao.deleteByName(name)

    fun delete(hand: Hand) = handDao.delete(hand)

    fun deleteAll() = handDao.deleteAll()
}