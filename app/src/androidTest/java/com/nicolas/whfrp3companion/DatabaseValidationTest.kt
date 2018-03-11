package com.nicolas.whfrp3companion

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.nicolas.whfrp3database.database
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.tables.PLAYER_TABLE_NAME
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.parseOpt
import org.jetbrains.anko.db.select
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class)
class DatabaseValidationTest {
    @Rule
    @JvmField
    val activity = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        appContext.database.use {
            select(PLAYER_TABLE_NAME).exec {
                val lol: Player? = parseOpt(classParser())
                assertNull(lol)
            }

            val insertRes: Long = insert(PLAYER_TABLE_NAME, "id" to 42, "name" to "John")
            assertEquals(insertRes, 42)

            select(PLAYER_TABLE_NAME).exec {
                val lol: Player? = parseOpt(classParser())
                assertNotNull(lol)
                assertEquals(lol!!.name, "John")
            }
        }
    }
}
