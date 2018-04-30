package com.nicolas.whfrp3database.player

import com.nicolas.whfrp3database.anko.AnkoPlayerRepository
import com.nicolas.whfrp3database.anko.database
import org.junit.After
import org.junit.Before
import org.robolectric.RuntimeEnvironment

abstract class AbstractAnkoPlayerRepositoryTest {
    protected lateinit var ankoPlayerRepository: AnkoPlayerRepository

    @Before
    fun setUp() {
        ankoPlayerRepository = AnkoPlayerRepository(RuntimeEnvironment.application)
    }

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}