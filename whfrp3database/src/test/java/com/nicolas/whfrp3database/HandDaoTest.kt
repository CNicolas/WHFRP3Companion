package com.nicolas.whfrp3database

import com.nicolas.whfrp3database.daos.hand.HandDao
import com.nicolas.whfrp3database.entities.hand.Hand
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class HandDaoTest {
    private var databaseHelper: DatabaseOpenHelper? = null

    @Before
    fun setUp() {
        databaseHelper = DatabaseOpenHelper(RuntimeEnvironment.application)
    }

    @Test
    fun should_add_a_hand() {
        val handDao = HandDao(databaseHelper!!)

        assertEquals(0, handDao.findAll().size)

        handDao.add(Hand("HandName", fortuneDicesCount = 2, id = 3))
        assertEquals(1, handDao.findAll().size)

        val foundHand = handDao.findById(3)
        assertNotNull(foundHand)
        assertEquals(2, foundHand!!.fortuneDicesCount)
    }

    @After
    fun tearDown() {
        databaseHelper?.close()
        databaseHelper?.deleteDatabase()
    }
}