package com.nicolas.database.actions

import com.nicolas.models.action.Action
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ActionsBlessingFileTest {
    private lateinit var blessingActions: List<Action>

    @Before
    fun setup() {
        blessingActions = ActionsFilesLoader().loadBlessingActions()
    }

    @Test
    fun should_load_actions_file() {
        assertThat(blessingActions.size).isEqualTo(0)
    }
}