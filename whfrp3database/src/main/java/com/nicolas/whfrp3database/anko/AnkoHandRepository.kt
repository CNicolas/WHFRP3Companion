package com.nicolas.whfrp3database.anko

import android.content.Context
import com.nicolas.models.hand.Hand
import com.nicolas.whfrp3database.HandRepository
import com.nicolas.whfrp3database.anko.daos.hand.HandDao

class AnkoHandRepository(context: Context) : HandRepository {
    private val handDao = HandDao(context.database)

    override fun save(hand: Hand): Hand = if (find(hand.name) == null) {
        add(hand)
    } else {
        update(hand)
    }

    override fun add(hand: Hand) = handDao.add(hand)!!

    override fun find(name: String): Hand? = handDao.findByName(name)

    override fun findAll(): List<Hand> = handDao.findAll()

    override fun update(hand: Hand) = handDao.update(hand)!!

    override fun delete(name: String) {
        handDao.deleteByName(name)
    }

    override fun delete(hand: Hand) {
        handDao.delete(hand)
    }

    fun deleteAll() = handDao.deleteAll()
}