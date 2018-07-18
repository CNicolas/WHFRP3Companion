package com.nicolas.database.files

import com.google.gson.Gson
import com.nicolas.database.genericType
import com.nicolas.models.item.enums.WeaponType.*
import com.nicolas.models.player.enums.Characteristic
import com.nicolas.models.skill.Skill
import com.nicolas.models.skill.SkillType
import com.nicolas.models.skill.Specialization
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class SkillsFileTest {
    private lateinit var allSkills: List<Skill>

    private val shootingSkill = Skill("Capacité de Tir",
            Characteristic.AGILITY,
            SkillType.BASIC,
            0,
            specializations = listOf(Specialization("Arcs", false, listOf(BOW)),
                    Specialization("Arbalètes", false, listOf(CROSSBOW, ONE_HANDED_CROSSBOW, REPEATING_CROSSBOW)),
                    Specialization("Armes de Jet", false),
                    Specialization("Armes à Poudre", false, listOf(HANDGUN, RIFLE, REPEATING_GUN)))
    )

    @Before
    fun setup() {
        val skillsReader = javaClass.classLoader.getResourceAsStream("skills.json").reader()
        allSkills = Gson().fromJson(skillsReader, genericType<List<Skill>>()) ?: listOf()
    }

    @Test
    fun should_load_skills_file() {
        assertThat(allSkills.size).isEqualTo(27)
    }

    @Test
    fun should_deserialize_skill() {
        val skill = allSkills.find { it.name == "Capacité de Tir" }
        assertThat(skill).isEqualTo(shootingSkill)
    }

    @Test
    fun should_serialize_skill() {
        val parsedShootingSkill = Gson().toJson(shootingSkill)
        val skill = Gson().fromJson<Skill>(parsedShootingSkill, genericType<Skill>())

        assertThat(skill).isEqualTo(shootingSkill)
    }
}