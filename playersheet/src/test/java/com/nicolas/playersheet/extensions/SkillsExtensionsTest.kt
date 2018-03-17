package com.nicolas.playersheet.extensions

import com.nicolas.whfrp3database.BuildConfig
import com.nicolas.whfrp3database.entities.player.enums.Characteristic.AGILITY
import com.nicolas.whfrp3database.entities.player.enums.Characteristic.STRENGTH
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
class SkillsExtensionsTest {
    private lateinit var allSkills: List<Skill>

    @Before
    fun setUp() {
        allSkills = loadSkills(RuntimeEnvironment.application)
    }

    @Test
    fun should_find_skill_by_extensions() {
        val strengthSkills = allSkills.findByCharacteristic(STRENGTH)
        assertThat(strengthSkills.size).isEqualTo(3)

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