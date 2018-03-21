package com.nicolas.whfrp3companion

import android.content.Intent
import com.nicolas.whfrp3companion.playersheet.PlayerCharacteristicsFragment
import com.nicolas.whfrp3companion.playersheet.PlayerSheetActivity
import com.nicolas.whfrp3database.entities.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class OpenPlayerSheetActivityTest {
    private lateinit var playerSheetActivity: PlayerSheetActivity

    @Before
    @Throws
    fun setUp() {
        val intent = Intent(ShadowApplication.getInstance().applicationContext, PlayerSheetActivity::class.java)
        intent.putExtra(PLAYER_INTENT_ARGUMENT, Player("Jack"))

        playerSheetActivity = Robolectric
                .buildActivity(PlayerSheetActivity::class.java, intent)
                .create()
                .resume()
                .get()
    }

    @Test
    fun should_be_initialized() {
        assertThat(playerSheetActivity).isNotNull()
    }

    @Test
    fun should_open() {
        val fragment = playerSheetActivity.supportFragmentManager.findFragmentById(R.id.playersheet_content_frame)
        assertThat(fragment is PlayerCharacteristicsFragment).isTrue()

        val playerCharacteristicsFragment = fragment as PlayerCharacteristicsFragment

        assertThat(playerCharacteristicsFragment.views.name).isNotNull()
        assertThat(playerCharacteristicsFragment.views.name.text.toString()).isEqualTo("Jack")
    }
}