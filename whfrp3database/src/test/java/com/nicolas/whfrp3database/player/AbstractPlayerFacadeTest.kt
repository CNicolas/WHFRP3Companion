package com.nicolas.whfrp3database.player

import com.nicolas.whfrp3database.anko.PlayerFacade
import com.nicolas.whfrp3database.anko.database
import org.junit.After
import org.junit.Before
import org.robolectric.RuntimeEnvironment

abstract class AbstractPlayerFacadeTest {
    protected lateinit var playerFacade: PlayerFacade

    @Before
    fun setUp() {
        playerFacade = PlayerFacade(RuntimeEnvironment.application)
    }

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}