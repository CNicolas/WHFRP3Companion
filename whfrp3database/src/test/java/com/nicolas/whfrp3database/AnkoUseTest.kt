package com.nicolas.whfrp3database

import android.database.sqlite.SQLiteDatabase
import com.nicolas.models.hand.Hand
import com.nicolas.models.player.Player
import com.nicolas.models.player.enums.Race.DWARF
import com.nicolas.models.player.enums.Race.HUMAN
import com.nicolas.whfrp3database.tables.HAND_TABLE_NAME
import com.nicolas.whfrp3database.tables.PLAYER_TABLE_NAME
import com.nicolas.whfrp3database.tables.parsers.HandParser
import com.nicolas.whfrp3database.tables.parsers.PlayerParser
import com.nicolas.whfrp3database.tables.parsers.toColumns
import com.nicolas.whfrp3database.tables.parsers.toPairs
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.parseOpt
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class AnkoUseTest {
    private var databaseHelper: DatabaseOpenHelper? = null

    @Before
    fun setUp() {
        databaseHelper = DatabaseOpenHelper(RuntimeEnvironment.application)
    }

    @Test
    fun should_use_hand() = databaseTest {
        select(HAND_TABLE_NAME).exec {
            val hand = parseOpt(HandParser())
            assertNull(hand)
        }

        val hand = Hand("HandName", challengeDicesCount = 1, id = 3)

        val insertRes: Long = insert(HAND_TABLE_NAME, *hand.toColumns().toPairs())
        assertEquals(3, insertRes)

        select(HAND_TABLE_NAME).exec {
            val foundHand = parseOpt(HandParser())
            assertNotNull(foundHand)
            assertEquals(3, foundHand!!.id)
            assertEquals("HandName", foundHand.name)
            assertEquals(1, foundHand.challengeDicesCount)
            assertEquals(0, foundHand.characteristicDicesCount)
        }

        hand.characteristicDicesCount = 4
        update(HAND_TABLE_NAME, *hand.toColumns().toPairs())
                .whereArgs("id = ${hand.id}")
                .exec()

        select(HAND_TABLE_NAME).exec {
            val foundHand = parseOpt(HandParser())
            assertNotNull(foundHand)
            assertEquals(3, foundHand!!.id)
            assertEquals("HandName", foundHand.name)
            assertEquals(1, foundHand.challengeDicesCount)
            assertEquals(4, foundHand.characteristicDicesCount)
        }
    }

    @Test
    fun should_use_player() = databaseTest {
        select(PLAYER_TABLE_NAME).exec {
            val player = parseOpt(PlayerParser())
            assertNull(player)
        }

        val player = Player("John", id = 44)

        val insertRes: Long = insert(PLAYER_TABLE_NAME, *player.toColumns().toPairs())
        assertEquals(44, insertRes)

        select(PLAYER_TABLE_NAME).exec {
            val foundPlayer = parseOpt(PlayerParser())
            assertNotNull(foundPlayer)
            assertEquals("John", foundPlayer!!.name)
            assertEquals(HUMAN, foundPlayer.race)
        }

        player.race = DWARF
        update(PLAYER_TABLE_NAME, *player.toColumns().toPairs())
                .whereArgs("id = ${player.id}")
                .exec()

        select(PLAYER_TABLE_NAME).exec {
            val foundPlayer = parseOpt(PlayerParser())
            assertNotNull(foundPlayer)
            assertEquals("John", foundPlayer!!.name)
            assertEquals(DWARF, foundPlayer.race)
        }
    }

    @After
    fun tearDown() {
        databaseHelper?.close()
        databaseHelper?.deleteDatabase()
    }

    private fun databaseTest(f: SQLiteDatabase.() -> Unit) =
            databaseHelper!!.writableDatabase.let(f)
}
