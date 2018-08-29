package com.nicolas.database.actions

import com.nicolas.models.action.Action
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ActionsSpellFileTest {
    private lateinit var spellActions: List<Action>

    @Before
    fun setup() {
        spellActions = ActionsFilesLoader().loadSpellActions()
    }

    @Test
    fun should_load_actions_file() {
        assertThat(spellActions.size).isEqualTo(0)
    }
}