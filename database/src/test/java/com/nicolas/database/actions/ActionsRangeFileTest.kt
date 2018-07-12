package com.nicolas.database.actions

import com.google.gson.Gson
import com.nicolas.database.genericType
import com.nicolas.models.action.Action
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ActionsRangeFileTest {
    private lateinit var rangeActions: List<Action>

    @Before
    fun setup() {
        val actionsReader = javaClass.classLoader.getResourceAsStream("actions_range.json").reader()
        rangeActions = Gson().fromJson(actionsReader, genericType<List<Action>>()) ?: listOf()
    }

    @Test
    fun should_load_actions_file() {
        assertThat(rangeActions.size).isEqualTo(6)
    }

    @Test
    fun should_calculate_damage_for_Tir() {
        val action = rangeActions.find { it.name == "Tir" }

        assertThat(action).isNotNull
    }
}