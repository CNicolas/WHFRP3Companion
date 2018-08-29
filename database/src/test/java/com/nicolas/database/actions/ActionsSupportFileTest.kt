package com.nicolas.database.actions

import com.nicolas.models.action.Action
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ActionsSupportFileTest {
    private lateinit var supportActions: List<Action>

    @Before
    fun setup() {
        supportActions = ActionsFilesLoader().loadSupportActions()
    }

    @Test
    fun should_load_actions_file() {
        assertThat(supportActions.size).isEqualTo(2)
    }
}