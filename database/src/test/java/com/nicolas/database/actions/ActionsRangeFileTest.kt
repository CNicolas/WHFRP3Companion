package com.nicolas.database.actions

import com.nicolas.models.action.Action
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ActionsRangeFileTest {
    private lateinit var rangeActions: List<Action>

    @Before
    fun setup() {
        rangeActions = ActionsFilesLoader().loadRangeActions()
    }

    @Test
    fun should_load_actions_file() {
        assertThat(rangeActions.size).isEqualTo(5)
    }
}