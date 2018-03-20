package com.nicolas.playersheet.extensions

import com.nicolas.whfrp3database.BuildConfig
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.Talent
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown.PASSIVE
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown.TALENT
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentType.AFFINITY
import com.nicolas.whfrp3database.extensions.findByType
import com.nicolas.whfrp3database.staticData.loadTalents
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class TalentsExtensionsTest {
    private lateinit var allTalents: List<Talent>

    @Before
    fun setUp() {
        allTalents = loadTalents(RuntimeEnvironment.application)
    }

    @Test
    fun should_find_talent_by_extensions() {
        val passiveTalents = allTalents.findByCooldown(PASSIVE)
        assertThat(passiveTalents.size).isEqualTo(84)

        val passiveAffinityTalents = passiveTalents.findByType(AFFINITY)
        assertThat(passiveAffinityTalents.size).isEqualTo(23)

        val passiveAffinityAetherTalents = passiveAffinityTalents.findByText("aéthérique")
        assertThat(passiveAffinityAetherTalents.size).isEqualTo(2)

        val oneTimeFilteredTalents = allTalents.findTalents("aéthérique", PASSIVE, AFFINITY)
        assertThat(oneTimeFilteredTalents.size).isEqualTo(2)
    }

    @Test
    fun should_find_talents_with_cooldown() {
        val exhaustibleTalents = allTalents.findByCooldown(TALENT)
        assertThat(exhaustibleTalents.size).isEqualTo(22)
    }

    @Test
    fun should_not_find_talent() {
        val emptyTalents = allTalents.findTalents("nothing")
        assertThat(emptyTalents).isEmpty()
    }
}