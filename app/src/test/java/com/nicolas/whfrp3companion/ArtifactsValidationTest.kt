package com.nicolas.whfrp3companion

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ArtifactsValidationTest {
    private var databaseHelper: DbHelper? = null

    @Before
    fun setUp() {
        databaseHelper = DbHelper(RuntimeEnvironment.application)
    }

    @Test
    fun should_save_player() {
//        val player = WarHammerContext.playerFacade.save(Player("Jimmy", race = WOOD_ELF))
//
//        assertThat(player.race).isEqualTo(WOOD_ELF)
//        assertThat(WarHammerContext.playerFacade.findAll()).isNotEmpty
    }
}

class DbHelper(private val context: Context, private val dbName: String = "test_db") : SQLiteOpenHelper(
        context,
        dbName,
        null,
        1
) {
    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun deleteDatabase() {
        context.deleteDatabase(dbName)
    }
}