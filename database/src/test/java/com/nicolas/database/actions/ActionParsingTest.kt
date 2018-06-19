package com.nicolas.database.actions

import com.google.gson.Gson
import com.nicolas.database.genericType
import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.action.Trait
import com.nicolas.models.action.condition.ActionCondition
import com.nicolas.models.action.condition.ActionConditionWeapon
import com.nicolas.models.action.effect.ActionFaceEffect
import com.nicolas.models.action.effect.Target
import com.nicolas.models.dice.Face.*
import com.nicolas.models.item.enums.Range
import com.nicolas.models.item.enums.WeaponCategory.*
import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class ActionParsingTest {
    private val rangeAttackSides: ActionSide = ActionSide(
            cooldown = 0,
            difficulty = listOf(),
            effects = hashMapOf(
                    SUCCESS to hashMapOf(1 to ActionFaceEffect(0),
                            3 to ActionFaceEffect(2)),
                    BOON to hashMapOf(2 to ActionFaceEffect(maneuver = true)),
                    BANE to hashMapOf(2 to ActionFaceEffect(canEngage = Target.TARGET)))

    )
    private val rangeAttack: Action =
            Action(
                    name = "Attaque à distance",
                    type = "ATTACK",
                    conservativeSide = rangeAttackSides,
                    recklessSide = rangeAttackSides,
                    traits = listOf(Trait.BASIC),
                    conditions = listOf(
                            ActionCondition(
                                    ActionConditionWeapon(
                                            listOf(RANGE, FIRE_ARM, REPEATING),
                                            equipped = true
                                    )
                            ),
                            ActionCondition(
                                    range = Range.ENGAGED,
                                    target = false
                            )
                    ),
                    skill = "Capacité de Tir",
                    targetDefense = true
            )
    private val rangeAttackJson: String = """{
    "name": "Attaque à distance",
    "type": "ATTACK",
    "traits": [ "BASIC" ],
    "skill": "Capacité de Tir",
    "targetDefense": true,
    "conditions": [
      {
        "weapon": {
          "categories": [ "RANGE", "FIRE_ARM", "REPEATING" ],
          "equipped": true
        }
      },
      { "range": "ENGAGED", "target": false }
    ],
    "conservativeSide": {
      "cooldown": 0,
      "difficulty": [ ],
      "effects": {
        "SUCCESS": {
          "1": { "damage": 0 },
          "3": { "damage": 2 }
        },
        "BOON": {
          "2": { "maneuver": true }
        },
        "BANE": {
          "2": { "canEngage": "TARGET" }
        }
      }
    },
    "recklessSide": {
      "cooldown": 0,
      "difficulty": [ ],
      "effects": {
        "SUCCESS": {
          "1": { "damage": 0 },
          "3": { "damage": 2 }
        },
        "BOON": {
          "2": { "maneuver": true }
        },
        "BANE": {
          "2": { "canEngage": "TARGET" }
        }
      }
    }
  }"""

    @Test
    fun should_deserialize_action() {
        val action = Gson().fromJson<Action>(rangeAttackJson, genericType<Action>())

        assertThat(action).isEqualTo(rangeAttack)
    }

    @Test
    fun should_serialize_action() {
        val parsedRangeAttack = Gson().toJson(rangeAttack)
        val action = Gson().fromJson<Action>(parsedRangeAttack, genericType<Action>())
        println(parsedRangeAttack)
        assertThat(action).isEqualTo(rangeAttack)
    }
}