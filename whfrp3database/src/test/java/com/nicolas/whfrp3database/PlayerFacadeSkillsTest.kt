package com.nicolas.whfrp3database

import com.nicolas.whfrp3database.entities.player.Player
import com.nicolas.whfrp3database.entities.player.enums.Characteristic.*
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.SkillType
import com.nicolas.whfrp3database.extensions.skills.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PlayerFacadeSkillsTest {
    private lateinit var playerFacade: PlayerFacade

    @Before
    fun setUp() {
        playerFacade = PlayerFacade(RuntimeEnvironment.application)
    }

    @Test
    fun should_add_advanced_skill_to_player() {
        val player = playerFacade.add(Player("John"))
        assertThat(player.name).isEqualTo("John")
        assertThat(player.skills.size).isEqualTo(18)

        val advancedSkills = playerFacade.advancedSkills
        println(advancedSkills)
        assertThat(advancedSkills.map { it.type }.distinct()).isEqualTo(listOf(SkillType.ADVANCED))
        player.addSkill(advancedSkills[0])

        val updatedPlayer = playerFacade.update(player)
        assertThat(updatedPlayer.skills.size).isEqualTo(19)
    }

    @Test
    fun should_update_level_of_skill_of_a_player() {
        val player = playerFacade.add(Player("John"))
        assertThat(player.name).isEqualTo("John")
        assertThat(player.skills.size).isEqualTo(18)

        val fight = player.getSkillByName("capacité de combat")
        assertThat(fight).isNotNull()
        assertThat(fight!!.level).isEqualTo(0)
        assertThat(fight.getSpecializationByName("Armes d’Hast")?.isSpecialized).isFalse()

        fight.level = 2
        fight.getSpecializationByName("Armes d’Hast")?.isSpecialized = true

        val updatedPlayer = playerFacade.update(player)
        assertThat(updatedPlayer.name).isEqualTo("John")
        assertThat(updatedPlayer.skills.size).isEqualTo(18)

        val newSkill = updatedPlayer.getSkillByName("capacité de combat")
        assertThat(newSkill).isNotNull()
        assertThat(newSkill!!.type).isEqualTo(SkillType.BASIC)
        assertThat(newSkill.characteristic).isEqualTo(STRENGTH)
        assertThat(newSkill.level).isEqualTo(2)
        assertThat(newSkill.getSpecializationByName("Armes d’Hast")?.isSpecialized).isTrue()
        assertThat(updatedPlayer.getSpecializations()).isNotEmpty
        assertThat(updatedPlayer.getSpecializationByName("Armes d’Hast")?.isSpecialized).isTrue()
    }

    @Test
    fun should_get_skills_by_characteristic() {
        val player = playerFacade.add(Player("John"))
        val strengthSkills = player.getSkillsByCharacteristic(STRENGTH)
        val toughnessSkills = player.getSkillsByCharacteristic(TOUGHNESS)
        val agilitySkills = player.getSkillsByCharacteristic(AGILITY)
        val intelligenceSkills = player.getSkillsByCharacteristic(INTELLIGENCE)
        val willpowerSkills = player.getSkillsByCharacteristic(WILLPOWER)
        val fellowshipSkills = player.getSkillsByCharacteristic(FELLOWSHIP)

        assertThat(strengthSkills).isNotEmpty
        assertThat(strengthSkills.size).isEqualTo(3)

        assertThat(toughnessSkills).isNotEmpty
        assertThat(toughnessSkills.size).isEqualTo(1)

        assertThat(agilitySkills).isNotEmpty
        assertThat(agilitySkills.size).isEqualTo(5)

        assertThat(intelligenceSkills).isNotEmpty
        assertThat(intelligenceSkills.size).isEqualTo(4)

        assertThat(willpowerSkills).isNotEmpty
        assertThat(willpowerSkills.size).isEqualTo(1)

        assertThat(fellowshipSkills).isNotEmpty
        assertThat(fellowshipSkills.size).isEqualTo(4)
    }

    @Test
    fun should_map_skill_with_specializations() {
        val specializationsBySkill = playerFacade.allSpecializations
        val shootingSkill = specializationsBySkill.keys.first { it.name == "Capacité de Tir" }
        val specializations = specializationsBySkill[shootingSkill]!!

        assertThat(specializations.size).isEqualTo(4)
        assertThat(specializations.map { it.name }).containsExactly("Arcs", "Arbalètes", "Armes de Jet", "Armes à Poudre")
    }

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}