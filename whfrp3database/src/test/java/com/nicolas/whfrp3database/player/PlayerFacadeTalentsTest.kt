package com.nicolas.whfrp3database.player

import com.nicolas.whfrp3database.BuildConfig
import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentCooldown.PASSIVE
import com.nicolas.whfrp3database.entities.player.playerLinked.talent.TalentType.FAITH
import com.nicolas.whfrp3database.extensions.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE, assetDir = "src/test/assets")
class PlayerFacadeTalentsTest : AbstractPlayerFacadeTest() {
    @Test
    fun should_add_passive_faith_talent() {
        val player = playerFacade.add(Player("John"))
        assertThat(player.talents).isEmpty()

        val passiveTalents = playerFacade.passiveTalents
        val selectedTalent = passiveTalents.first { it.type == FAITH }
        player.addTalent(selectedTalent)

        val updatedPlayer = playerFacade.update(player)
        assertThat(updatedPlayer.talents.size).isEqualTo(1)
        assertThat(updatedPlayer.talents[0].cooldown).isEqualTo(PASSIVE)
        assertThat(updatedPlayer.talents[0].type).isEqualTo(FAITH)
        println(updatedPlayer.talents[0])

        assertThat(updatedPlayer.getTalentsByType(FAITH)[0]).isEqualToComparingFieldByField(selectedTalent)
        assertThat(updatedPlayer.getPassiveTalents()[0]).isEqualToComparingFieldByField(selectedTalent)
    }

    @Test
    fun should_equip_a_talent() {
        val player = playerFacade.add(Player("John"))
        assertThat(player.talents).isEmpty()

        val exhaustibleTalent = playerFacade.exhaustibleTalents[0]
        player.addTalent(exhaustibleTalent)

        val updatedPlayer1 = playerFacade.update(player)
        assertThat(updatedPlayer1.talents.size).isEqualTo(1)
        assertThat(updatedPlayer1.talents[0].name).isEqualTo(exhaustibleTalent.name)
        assertThat(updatedPlayer1.talents[0].cooldown).isEqualTo(TalentCooldown.TALENT)
        assertThat(updatedPlayer1.talents[0].isEquipped).isFalse()

        player.equipTalent(exhaustibleTalent)

        val updatedPlayer2 = playerFacade.update(player)
        assertThat(updatedPlayer2.talents.size).isEqualTo(1)
        assertThat(updatedPlayer2.talents[0].name).isEqualTo(exhaustibleTalent.name)
        assertThat(updatedPlayer2.talents[0].cooldown).isEqualTo(TalentCooldown.TALENT)
        assertThat(updatedPlayer2.talents[0].isEquipped).isTrue()
        assertThat(updatedPlayer2.getExhaustibleTalents()[0]).isEqualToComparingFieldByField(exhaustibleTalent)

        exhaustibleTalent.isEquipped = false

        val updatedPlayer3 = playerFacade.update(player)
        assertThat(updatedPlayer3.talents.size).isEqualTo(1)
        assertThat(updatedPlayer3.talents[0].name).isEqualTo(exhaustibleTalent.name)
        assertThat(updatedPlayer3.talents[0].cooldown).isEqualTo(TalentCooldown.TALENT)
        assertThat(updatedPlayer3.talents[0].isEquipped).isFalse()
    }

    @Test
    fun should_load_talent_by_type_and_passive() {
        val passiveFaithTalent = playerFacade.passiveTalents.first { it.type == FAITH }
        val faithPassiveTalent = playerFacade.talents.filter { it.type == FAITH }.first { it.cooldown == PASSIVE }

        assertThat(passiveFaithTalent).isEqualToComparingFieldByField(faithPassiveTalent)
    }
}