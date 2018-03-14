package com.nicolas.playersheet.extensions

import com.nicolas.whfrp3database.BuildConfig
import com.nicolas.whfrp3database.entities.player.enums.Characteristic
import com.nicolas.whfrp3database.entities.player.playerLinked.skill.Skill
import com.nicolas.whfrp3database.staticData.loadSkills
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class SpecializationsExtensionsTest {
    private lateinit var allSkills: List<Skill>

    @Before
    fun setUp() {
        allSkills = loadSkills(RuntimeEnvironment.application)!!
    }

    @Test
    fun should_find_sspecialization_by_extensions() {
        val oneTimeFilteredSpecializations = allSkills.findSpecializations("ation", Characteristic.TOUGHNESS)
        assertThat(oneTimeFilteredSpecializations.size).isEqualTo(1)
        val skill = oneTimeFilteredSpecializations.findSkillByName("Résistance")
        assertThat(skill).isNotNull()
        assertThat(oneTimeFilteredSpecializations[skill]!!.size).isEqualTo(1)
        assertThat(oneTimeFilteredSpecializations[skill]!![0].name).isEqualTo("Récupération Après l'Effort")
    }

    @Test
    fun should_all_specializations() {
        val allSpecializationsMap = allSkills.findSpecializations()
        assertThat(allSpecializationsMap.size).isEqualTo(27)
        assertThat(allSpecializationsMap.flatMap { it.value }.size).isGreaterThanOrEqualTo(3 * 27)
    }

    @Test
    fun should_return_specializations_of_shooting_skill() {
        val shootingSkill = allSkills.findSkills("Capacité de Tir")[0]
        val specializationsBySkillName = allSkills.findSpecializations(skillName = "Capacité de Tir")
        val shootingSkillNameSpecializations = specializationsBySkillName[shootingSkill]!!
        assertThat(shootingSkillNameSpecializations.size).isEqualTo(4)
        assertThat(shootingSkillNameSpecializations[0].name).isEqualTo("Arcs")

        val specializationsBySkill = allSkills.findSpecializations(skill = shootingSkill)
        val shootingSkillSpecializations = specializationsBySkill[shootingSkill]!!
        assertThat(shootingSkillSpecializations.size).isEqualTo(4)
        assertThat(shootingSkillSpecializations[0].name).isEqualTo("Arcs")
    }

    @Test
    fun should_not_find_specialization() {
        val emptyMap = allSkills.findSpecializations("nothing")
        assertThat(emptyMap.size).isEqualTo(0)
        assertThat(emptyMap).isEmpty()
    }
}