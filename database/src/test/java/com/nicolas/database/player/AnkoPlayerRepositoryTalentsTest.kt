package com.nicolas.database.player

import com.nicolas.database.BuildConfig
import com.nicolas.models.extensions.*
import com.nicolas.models.player.Player
import com.nicolas.models.player.talent.TalentCooldown
import com.nicolas.models.player.talent.TalentCooldown.PASSIVE
import com.nicolas.models.player.talent.TalentType.FAITH
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, assetDir = "src/test/assets")
class AnkoPlayerRepositoryTalentsTest : AbstractAnkoPlayerRepositoryTest() {
    @Test
    fun should_add_passive_faith_talent() {
        val player = ankoPlayerRepository.add(Player("John"))
        assertThat(player.talents).isEmpty()

        val passiveTalents = allTalents.passive
        val selectedTalent = passiveTalents.first { it.type == FAITH }
        player.addTalent(selectedTalent)

        val updatedPlayer = ankoPlayerRepository.update(player)
        assertThat(updatedPlayer.talents.size).isEqualTo(1)
        assertThat(updatedPlayer.talents[0].cooldown).isEqualTo(PASSIVE)
        assertThat(updatedPlayer.talents[0].type).isEqualTo(FAITH)
        println(updatedPlayer.talents[0])

        assertThat(updatedPlayer.getTalentsByType(FAITH)[0]).isEqualToComparingFieldByField(selectedTalent)
        assertThat(updatedPlayer.getPassiveTalents()[0]).isEqualToComparingFieldByField(selectedTalent)
    }

    @Test
    fun should_equip_a_talent() {
        val player = ankoPlayerRepository.add(Player("John"))
        assertThat(player.talents).isEmpty()

        val exhaustibleTalent = allTalents.exhaustible[0]
        player.addTalent(exhaustibleTalent)

        val updatedPlayer1 = ankoPlayerRepository.update(player)
        assertThat(updatedPlayer1.talents.size).isEqualTo(1)
        assertThat(updatedPlayer1.talents[0].name).isEqualTo(exhaustibleTalent.name)
        assertThat(updatedPlayer1.talents[0].cooldown).isEqualTo(TalentCooldown.TALENT)
        assertThat(updatedPlayer1.talents[0].isEquipped).isFalse()

        player.equipTalent(exhaustibleTalent)

        val updatedPlayer2 = ankoPlayerRepository.update(player)
        assertThat(updatedPlayer2.talents.size).isEqualTo(1)
        assertThat(updatedPlayer2.talents[0].name).isEqualTo(exhaustibleTalent.name)
        assertThat(updatedPlayer2.talents[0].cooldown).isEqualTo(TalentCooldown.TALENT)
        assertThat(updatedPlayer2.talents[0].isEquipped).isTrue()
        assertThat(updatedPlayer2.getExhaustibleTalents()[0]).isEqualToComparingFieldByField(exhaustibleTalent)

        exhaustibleTalent.isEquipped = false

        val updatedPlayer3 = ankoPlayerRepository.update(player)
        assertThat(updatedPlayer3.talents.size).isEqualTo(1)
        assertThat(updatedPlayer3.talents[0].name).isEqualTo(exhaustibleTalent.name)
        assertThat(updatedPlayer3.talents[0].cooldown).isEqualTo(TalentCooldown.TALENT)
        assertThat(updatedPlayer3.talents[0].isEquipped).isFalse()
    }

    @Test
    fun should_load_talent_by_type_and_passive() {
        val passiveFaithTalent = allTalents.passive.first { it.type == FAITH }
        val faithPassiveTalent = allTalents.filter { it.type == FAITH }.first { it.cooldown == PASSIVE }

        assertThat(passiveFaithTalent).isEqualToComparingFieldByField(faithPassiveTalent)
    }
}