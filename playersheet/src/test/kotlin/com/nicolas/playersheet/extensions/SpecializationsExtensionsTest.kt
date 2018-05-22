package com.nicolas.playersheet.extensions

import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.player.skill.Skill
import com.nicolas.models.player.skill.Specialization
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class SpecializationsExtensionsTest {
    private lateinit var allSkills: List<Skill>

    @Before
    fun setUp() {
        allSkills = listOf(Skill("Résistance", Characteristic.TOUGHNESS, specializations = listOf(Specialization("Récupération Après l'Effort"))),
                Skill("Capacité de Tir", Characteristic.AGILITY, specializations = listOf(Specialization("Arcs"),Specialization("Arbalètes"))))
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
    fun should_return_specializations_of_shooting_skill() {
        val shootingSkill = allSkills.findSkills("Capacité de Tir")[0]
        val specializationsBySkillName = allSkills.findSpecializations(skillName = "Capacité de Tir")
        val shootingSkillNameSpecializations = specializationsBySkillName[shootingSkill]!!
        assertThat(shootingSkillNameSpecializations.size).isEqualTo(2)
        assertThat(shootingSkillNameSpecializations[0].name).isEqualTo("Arcs")

        val specializationsBySkill = allSkills.findSpecializations(skill = shootingSkill)
        val shootingSkillSpecializations = specializationsBySkill[shootingSkill]!!
        assertThat(shootingSkillSpecializations.size).isEqualTo(2)
        assertThat(shootingSkillSpecializations[0].name).isEqualTo("Arcs")
    }

    @Test
    fun should_not_find_specialization() {
        val emptyMap = allSkills.findSpecializations("nothing")
        assertThat(emptyMap.size).isEqualTo(0)
        assertThat(emptyMap).isEmpty()
    }
}