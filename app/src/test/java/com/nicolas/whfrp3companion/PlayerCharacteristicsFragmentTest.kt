package com.nicolas.whfrp3companion

import android.widget.TextView
import com.nicolas.whfrp3companion.playersheet.PlayerCharacteristicsFragment
import com.nicolas.whfrp3database.entities.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PlayerCharacteristicsFragmentTest {
    @Test
    fun should_be_initialized() {
        val fragment = PlayerCharacteristicsFragment.newInstance(Player("John"))

        startFragment(fragment)
        assertThat(fragment).isNotNull()

        val fragmentView = fragment.view
        assertThat(fragmentView).isNotNull()
        val playerNameTextView = fragmentView!!.findViewById(R.id.player_name) as TextView
        assertThat(playerNameTextView.text).isEqualTo("John")

    }
}