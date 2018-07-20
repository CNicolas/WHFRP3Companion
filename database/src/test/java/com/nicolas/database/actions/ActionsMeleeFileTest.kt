package com.nicolas.database.actions

import com.nicolas.models.action.Action
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ActionsMeleeFileTest {
    private lateinit var meleeActions: List<Action>

    @Before
    fun setup() {
        meleeActions = ActionsFilesLoader().loadMeleeActions()
    }

    @Test
    fun should_load_actions_file() {
        assertThat(meleeActions.size).isEqualTo(5)
    }
}