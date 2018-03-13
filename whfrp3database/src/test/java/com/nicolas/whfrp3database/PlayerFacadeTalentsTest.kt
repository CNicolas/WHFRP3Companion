//package warhammer.database.player
//
//import org.assertj.core.api.Assertions.assertThat
//import org.testng.annotations.BeforeMethod
//import org.testng.annotations.Test
//import warhammer.database.PlayerFacade
//import warhammer.database.entities.player.Player
//import warhammer.database.entities.player.playerLinked.talent.TalentCooldown
//import warhammer.database.entities.player.playerLinked.talent.TalentCooldown.PASSIVE
//import warhammer.database.entities.player.playerLinked.talent.TalentType.FAITH
//import warhammer.database.extensions.talents.*
//import warhammer.database.staticData.getExhaustibleTalents
//import warhammer.database.staticData.getPassiveTalents
//import warhammer.database.staticData.getTalentsByType
//
//class PlayerFacadeTalentsTest {
//    private val playerFacade = PlayerFacade(
//            databaseUrl = "jdbc:sqlite:testSqlite:?mode=memory&cache=shared",
//            driver = "org.sqlite.JDBC"
//    )
//
//    @BeforeMethod
//    fun clearBeforeTest() {
//        playerFacade.deleteAll()
//    }
//
//    @Test
//    fun should_add_passive_faith_talent() {
//        val player = playerFacade.save(Player("John"))
//        assertThat(player.talents).isEmpty()
//
//        val passiveTalents = getPassiveTalents()
//        val selectedTalent = passiveTalents.first { it.type == FAITH }
//        player.addTalent(selectedTalent)
//
//        val updatedPlayer = playerFacade.save(player)
//        assertThat(updatedPlayer.talents.size).isEqualTo(1)
//        assertThat(updatedPlayer.talents[0].cooldown).isEqualTo(PASSIVE)
//        assertThat(updatedPlayer.talents[0].type).isEqualTo(FAITH)
//        println(updatedPlayer.talents[0])
//
//        assertThat(updatedPlayer.getTalentsByType(FAITH)[0]).isEqualToComparingFieldByField(selectedTalent)
//        assertThat(updatedPlayer.getPassiveTalents()[0]).isEqualToComparingFieldByField(selectedTalent)
//    }
//
//    @Test
//    fun should_equip_a_talent() {
//        val player = playerFacade.save(Player("John"))
//        assertThat(player.talents).isEmpty()
//
//        val exhaustibleTalent = getExhaustibleTalents()[0]
//        player.addTalent(exhaustibleTalent)
//
//        val updatedPlayer1 = playerFacade.save(player)
//        assertThat(updatedPlayer1.talents.size).isEqualTo(1)
//        assertThat(updatedPlayer1.talents[0].name).isEqualTo(exhaustibleTalent.name)
//        assertThat(updatedPlayer1.talents[0].cooldown).isEqualTo(TalentCooldown.TALENT)
//        assertThat(updatedPlayer1.talents[0].isEquipped).isFalse()
//
//        player.equipTalent(exhaustibleTalent)
//
//        val updatedPlayer2 = playerFacade.save(player)
//        assertThat(updatedPlayer2.talents.size).isEqualTo(1)
//        assertThat(updatedPlayer2.talents[0].name).isEqualTo(exhaustibleTalent.name)
//        assertThat(updatedPlayer2.talents[0].cooldown).isEqualTo(TalentCooldown.TALENT)
//        assertThat(updatedPlayer2.talents[0].isEquipped).isTrue()
//        assertThat(updatedPlayer2.getExhaustibleTalents()[0]).isEqualToComparingFieldByField(exhaustibleTalent)
//
//        exhaustibleTalent.isEquipped = false
//
//        val updatedPlayer3 = playerFacade.save(player)
//        assertThat(updatedPlayer3.talents.size).isEqualTo(1)
//        assertThat(updatedPlayer3.talents[0].name).isEqualTo(exhaustibleTalent.name)
//        assertThat(updatedPlayer3.talents[0].cooldown).isEqualTo(TalentCooldown.TALENT)
//        assertThat(updatedPlayer3.talents[0].isEquipped).isFalse()
//    }
//
//    @Test
//    fun should_load_talent_by_type_and_passive() {
//        val passiveFaithTalent = getPassiveTalents().first { it.type == FAITH }
//        val faithPassiveTalent = getTalentsByType(FAITH).first { it.cooldown == PASSIVE }
//
//        assertThat(passiveFaithTalent).isEqualToComparingFieldByField(faithPassiveTalent)
//    }
//}