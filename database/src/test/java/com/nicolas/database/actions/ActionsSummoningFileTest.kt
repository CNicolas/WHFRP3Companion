package com.nicolas.database.actions

import com.nicolas.models.action.Action
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ActionsSummoningFileTest {
    private lateinit var summoningActions: List<Action>

    @Before
    fun setup() {
        summoningActions = ActionsFilesLoader().loadSummoningActions()
    }

    @Test
    fun should_load_actions_file() {
        assertThat(summoningActions.size).isEqualTo(0)
    }
}