package com.nicolas.whfrp3database

import android.database.sqlite.SQLiteDatabase
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.PlayerParser
import com.nicolas.whfrp3database.entities.player.toColumns
import com.nicolas.whfrp3database.tables.PLAYER_TABLE_NAME
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.parseOpt
import org.jetbrains.anko.db.select
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
class PlayerTableTest {
    private var databaseHelper: DatabaseOpenHelper? = null

    @Before
    fun setUp() {
        databaseHelper = DatabaseOpenHelper(RuntimeEnvironment.application)
    }

    @Test
    fun should_use_player() = databaseTest {
        select(PLAYER_TABLE_NAME).exec {
            val player = parseOpt(PlayerParser())
            assertNull(player)
        }

        val player = Player("John", id = databaseHelper!!.nextAvailableId(this, PLAYER_TABLE_NAME))

        val insertRes: Long = insert(PLAYER_TABLE_NAME, *player.toColumns())
        assertEquals(1, insertRes)

        select(PLAYER_TABLE_NAME).exec {
            val foundPlayer = parseOpt(PlayerParser())
            assertNotNull(foundPlayer)
            assertEquals("John", foundPlayer!!.name)
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
