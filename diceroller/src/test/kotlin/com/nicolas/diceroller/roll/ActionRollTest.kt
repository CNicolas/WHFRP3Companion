package com.nicolas.diceroller.roll

import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.action.ActionType
import com.nicolas.models.action.effect.ActionFaceEffect
import com.nicolas.models.action.effect.effect
import com.nicolas.models.action.effect.face
import com.nicolas.models.action.effect.faceCount
import com.nicolas.models.dice.Face.*
import com.nicolas.models.player.enums.Stance.CONSERVATIVE
import com.nicolas.models.player.enums.Stance.RECKLESS
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ActionRollTest {
    private val action = Action(
            name = "Attack",
            type = ActionType.MELEE_ATTACK,
            traits = listOf(),
            conservativeSide = ActionSide(CONSERVATIVE,
                    effects = hashMapOf(
                            SUCCESS to hashMapOf(
                                    1 to listOf(ActionFaceEffect(1)),
                                    3 to listOf(ActionFaceEffect(3))),
                            BOON to hashMapOf(
                                    2 to listOf(ActionFaceEffect(2), ActionFaceEffect(critical = 1))
                            )
                    )
            ),
            recklessSide = ActionSide(RECKLESS,
                    effects = hashMapOf(
                            SUCCESS to hashMapOf(
                                    1 to listOf(ActionFaceEffect(0)),
                                    3 to listOf(ActionFaceEffect(2))
                            ),
                            BOON to hashMapOf(
                                    1 to listOf(ActionFaceEffect(1)),
                                    2 to listOf(ActionFaceEffect(critical = 1))
                            ),
                            CHAOS to hashMapOf(
                                    1 to listOf(ActionFaceEffect(exhaustion = 1)),
                                    2 to listOf(ActionFaceEffect(damage = -1))
                            )
                    )
            )
    )

    @Test
    fun should_sum_conservative_effects_correctly() {
        val rollResult = RollResult(listOf(SUCCESS, SUCCESS, SUCCESS, SUCCESS, SUCCESS, BOON, BOON, BOON, BOON, BOON, BOON, BOON))
        val (actionRollResult, resultingReport, effectsList) = action.getRollResult(CONSERVATIVE, rollResult)

        println(effectsList)
        println(actionRollResult)

        assertThat(effectsList.size).isEqualTo(4)
        assertThat(effectsList.filter { it.face == SUCCESS }.size).isEqualTo(2)
        assertThat(effectsList.filter { it.face == SUCCESS }[0].faceCount).isEqualTo(1)
        assertThat(effectsList.filter { it.face == SUCCESS }[0].effect).isEqualTo(ActionFaceEffect(1))
        assertThat(effectsList.filter { it.face == SUCCESS }[1].faceCount).isEqualTo(3)
        assertThat(effectsList.filter { it.face == SUCCESS }[1].effect).isEqualTo(ActionFaceEffect(3))
        assertThat(effectsList.filter { it.face == BOON }.size).isEqualTo(2)
        assertThat(effectsList.filter { it.face == BOON }[0].faceCount).isEqualTo(2)
        assertThat(effectsList.filter { it.face == BOON }[0].effect).isEqualTo(ActionFaceEffect(2))
        assertThat(effectsList.filter { it.face == BOON }[1].faceCount).isEqualTo(2)
        assertThat(effectsList.filter { it.face == BOON }[1].effect).isEqualTo(ActionFaceEffect(critical = 1))

        assertThat(actionRollResult.damage).isEqualTo(6)
        assertThat(actionRollResult.critical).isEqualTo(1)

        assertThat(resultingReport[SUCCESS]).isEqualTo(1)
        assertThat(resultingReport[BOON]).isEqualTo(3)
    }

    @Test
    fun should_sum_reckless_effects_correctly() {
        val rollResult = RollResult(listOf(SUCCESS, SUCCESS, SUCCESS, BOON, BOON, BOON, BOON, CHAOS, CHAOS, CHAOS, CHAOS))
        val (actionRollResult, resultingReport, effectsList) = action.getRollResult(RECKLESS, rollResult)

        println(effectsList)
        println(actionRollResult)

        assertThat(effectsList.size).isEqualTo(5)
        assertThat(effectsList.filter { it.face == SUCCESS }.size).isEqualTo(1)
        assertThat(effectsList.filter { it.face == SUCCESS }[0].faceCount).isEqualTo(3)
        assertThat(effectsList.filter { it.face == SUCCESS }[0].effect).isEqualTo(ActionFaceEffect(2))
        assertThat(effectsList.filter { it.face == BOON }.size).isEqualTo(2)
        assertThat(effectsList.filter { it.face == BOON }[0].faceCount).isEqualTo(1)
        assertThat(effectsList.filter { it.face == BOON }[0].effect).isEqualTo(ActionFaceEffect(1))
        assertThat(effectsList.filter { it.face == BOON }[1].faceCount).isEqualTo(2)
        assertThat(effectsList.filter { it.face == BOON }[1].effect).isEqualTo(ActionFaceEffect(critical = 1))
        assertThat(effectsList.filter { it.face == CHAOS }.size).isEqualTo(2)
        assertThat(effectsList.filter { it.face == CHAOS }[0].faceCount).isEqualTo(1)
        assertThat(effectsList.filter { it.face == CHAOS }[0].effect).isEqualTo(ActionFaceEffect(exhaustion = 1))
        assertThat(effectsList.filter { it.face == CHAOS }[1].faceCount).isEqualTo(2)
        assertThat(effectsList.filter { it.face == CHAOS }[1].effect).isEqualTo(ActionFaceEffect(damage = -1))

        assertThat(actionRollResult.damage).isEqualTo(2)
        assertThat(actionRollResult.critical).isEqualTo(1)

        assertThat(resultingReport[BOON]).isEqualTo(1)
        assertThat(resultingReport[CHAOS]).isEqualTo(1)
    }
}