package com.nicolas.database.actions

import com.google.gson.Gson
import com.nicolas.database.genericType
import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.action.ActionType
import com.nicolas.models.action.Trait
import com.nicolas.models.action.condition.ActionCondition
import com.nicolas.models.action.condition.ActionConditionWeapon
import com.nicolas.models.action.effect.ActionFaceEffect
import com.nicolas.models.action.effect.Target
import com.nicolas.models.dice.Face.*
import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.WeaponCategory
import com.nicolas.models.player.enums.Stance.CONSERVATIVE
import com.nicolas.models.player.enums.Stance.RECKLESS
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ActionsFileTest {
    private lateinit var allActions: List<Action>

    private val rangeAttack: Action =
            Action(
                    name = "Tir",
                    type = ActionType.RANGE_ATTACK,
                    conservativeSide = ActionSide(
                            stance = CONSERVATIVE,
                            cooldown = 0,
                            difficulty = listOf(),
                            effects = hashMapOf(
                                    SUCCESS to hashMapOf(1 to ActionFaceEffect(0),
                                            3 to ActionFaceEffect(2)),
                                    BOON to hashMapOf(2 to ActionFaceEffect(maneuver = true)),
                                    BANE to hashMapOf(2 to ActionFaceEffect(canEngage = Target.TARGET)))
                    ),
                    recklessSide = ActionSide(
                            stance = RECKLESS,
                            cooldown = 0,
                            difficulty = listOf(),
                            effects = hashMapOf(
                                    SUCCESS to hashMapOf(1 to ActionFaceEffect(0),
                                            3 to ActionFaceEffect(2)),
                                    BOON to hashMapOf(2 to ActionFaceEffect(maneuver = true)),
                                    BANE to hashMapOf(2 to ActionFaceEffect(canEngage = Target.TARGET)))
                    ),
                    traits = listOf(Trait.BASIC),
                    conditions = listOf(
                            ActionCondition(
                                    ActionConditionWeapon(
                                            listOf(WeaponCategory.RANGE, WeaponCategory.FIRE_ARM, WeaponCategory.REPEATING),
                                            equipped = true
                                    )
                            ),
                            ActionCondition(
                                    range = Range.ENGAGED,
                                    target = Target.NONE
                            )
                    ),
                    skill = "Capacité de Tir",
                    targetDefense = true
            )

    @Before
    fun setup() {
        val actionsReader = javaClass.classLoader.getResourceAsStream("actions.json").reader()
        allActions = Gson().fromJson(actionsReader, genericType<List<Action>>()) ?: listOf()
    }

    @Test
    fun should_load_actions_file() {
        assertThat(allActions.size).isEqualTo(6)
    }

    @Test
    fun should_deserialize_action() {
        val action = allActions.find { it.name == "Tir" }
        assertThat(action).isEqualTo(rangeAttack)
    }

    @Test
    fun should_serialize_action() {
        val parsedRangeAttack = Gson().toJson(rangeAttack)
        val action = Gson().fromJson<Action>(parsedRangeAttack, genericType<Action>())

        assertThat(action).isEqualTo(rangeAttack)
    }
}