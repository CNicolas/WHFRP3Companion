package com.nicolas.playersheet.extensions

import com.nicolas.models.player.enums.Characteristic.AGILITY
import com.nicolas.models.player.enums.Characteristic.STRENGTH
import com.nicolas.models.player.playerLinked.skill.Skill
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SkillsExtensionsTest {
    private val allSkills: List<Skill> = listOf(Skill("Capacité de Combat", STRENGTH),
            Skill("Capacité de Tir", AGILITY))

    @Test
    fun should_find_skill_by_extensions() {
        val strengthSkills = allSkills.findByCharacteristic(STRENGTH)
        assertThat(strengthSkills.size).isEqualTo(1)

        val fightSkill = strengthSkills.findByText("capacit")
        assertThat(fightSkill.size).isEqualTo(1)
        assertThat(fightSkill[0].name).isEqualTo("Capacité de Combat")

        val oneTimeFilteredSkills = allSkills.findSkills("caPAcité", AGILITY)
        assertThat(oneTimeFilteredSkills.size).isEqualTo(1)
        assertThat(oneTimeFilteredSkills[0].name).isEqualTo("Capacité de Tir")
    }

    @Test
    fun should_not_find_skill() {
        val emptySkills = allSkills.findSkills("nothing")
        assertThat(emptySkills).isEmpty()
    }
}