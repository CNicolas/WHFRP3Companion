package com.nicolas.database.player

import com.nicolas.database.anko.AnkoPlayerRepository
import com.nicolas.database.anko.database
import com.nicolas.database.loadSkills
import com.nicolas.database.loadTalents
import com.nicolas.models.player.talent.Talent
import com.nicolas.models.skill.Skill
import org.junit.After
import org.junit.Before
import org.robolectric.RuntimeEnvironment

abstract class AbstractAnkoPlayerRepositoryTest {
    protected lateinit var ankoPlayerRepository: AnkoPlayerRepository

    protected lateinit var allSkills: List<Skill>
    protected lateinit var allTalents: List<Talent>

    @Before
    fun setUp() {
        ankoPlayerRepository = AnkoPlayerRepository(RuntimeEnvironment.application)
        allSkills = loadSkills(RuntimeEnvironment.application)
        allTalents = loadTalents(RuntimeEnvironment.application)
    }

    @After
    fun tearDown() {
        RuntimeEnvironment.application.database.close()
        RuntimeEnvironment.application.database.deleteDatabase()
    }
}