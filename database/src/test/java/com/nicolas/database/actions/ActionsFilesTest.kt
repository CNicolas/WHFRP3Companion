package com.nicolas.database.actions

import com.nicolas.models.action.Action
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class ActionsFilesTest {
    private lateinit var allActions: List<Action>

    @Before
    fun setup() {
        allActions = ActionsFilesLoader().loadActions()
    }

    @Test
    fun should_load_actions_file() {
        Assertions.assertThat(allActions.size).isEqualTo(12)
    }
}