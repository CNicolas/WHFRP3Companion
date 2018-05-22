package com.nicolas.models.extensions

import com.nicolas.models.player.talent.Talent
import com.nicolas.models.player.talent.TalentCooldown.PASSIVE
import com.nicolas.models.player.talent.TalentCooldown.TALENT
import com.nicolas.models.player.talent.TalentType.AFFINITY
import com.nicolas.models.player.talent.TalentType.REPUTATION
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TalentsExtensionsTest {
    private val allTalents: List<Talent> = listOf(Talent("Aéthérique", PASSIVE, AFFINITY, ""),
            Talent("Solid", TALENT, REPUTATION, ""))
    @Test
    fun should_find_talent_by_extensions() {
        val passiveTalents = allTalents.findByCooldown(PASSIVE)
        assertThat(passiveTalents.size).isEqualTo(1)

        val passiveAffinityTalents = passiveTalents.findByType(AFFINITY)
        assertThat(passiveAffinityTalents.size).isEqualTo(1)

        val passiveAffinityAetherTalents = passiveAffinityTalents.findByText("aéthérique")
        assertThat(passiveAffinityAetherTalents.size).isEqualTo(1)

        val oneTimeFilteredTalents = allTalents.findTalents("aéthérique", PASSIVE, AFFINITY)
        assertThat(oneTimeFilteredTalents.size).isEqualTo(1)
    }

    @Test
    fun should_find_talents_with_cooldown() {
        val exhaustibleTalents = allTalents.findByCooldown(TALENT)
        assertThat(exhaustibleTalents.size).isEqualTo(1)
    }

    @Test
    fun should_not_find_talent() {
        val emptyTalents = allTalents.findTalents("nothing")
        assertThat(emptyTalents).isEmpty()
    }
}