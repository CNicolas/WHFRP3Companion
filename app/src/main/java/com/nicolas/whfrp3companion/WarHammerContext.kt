package com.nicolas.whfrp3companion

import warhammer.database.HandFacade
import warhammer.database.PlayerFacade
import warhammer.playersheet.DATABASE_URL
import warhammer.playersheet.DRIVER

object WarHammerContext {
    val playerFacade = PlayerFacade(DATABASE_URL, DRIVER)
    val handFacade = HandFacade(DATABASE_URL, DRIVER)
}