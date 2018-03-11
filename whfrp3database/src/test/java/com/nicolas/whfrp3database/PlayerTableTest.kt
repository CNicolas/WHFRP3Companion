package com.nicolas.whfrp3database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.tables.PLAYER_TABLE_NAME
import org.jetbrains.anko.db.*
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import test.BuildConfig

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
            val lol: Player? = parseOpt(classParser())
            Assert.assertNull(lol)
        }

        val insertRes: Long = insert(PLAYER_TABLE_NAME, "id" to 42, "name" to "John")
        Assert.assertEquals(insertRes, 42)

        select(PLAYER_TABLE_NAME).exec {
            val lol: Player? = parseOpt(classParser())
            Assert.assertNotNull(lol)
            Assert.assertEquals(lol!!.name, "John")
        }
    }

    @After
    fun tearDown() {
        databaseHelper?.close()
        databaseHelper?.deleteDatabase()
    }
}