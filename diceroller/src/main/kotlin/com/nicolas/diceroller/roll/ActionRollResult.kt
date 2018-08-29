package com.nicolas.diceroller.roll

import com.nicolas.models.action.Action
import com.nicolas.models.action.ActionSide
import com.nicolas.models.action.effect.*
import com.nicolas.models.dice.Face
import com.nicolas.models.player.enums.Stance
import com.nicolas.models.player.enums.Stance.*

fun Action.getRollResult(stance: Stance, rollResult: RollResult): Triple<ActionFaceEffect, FacesReport, List<ActionEffect>> {
    val (resultingReport, effectsList) = getRollResultEffects(stance, rollResult)
    val finalEffect = effectsList.map { it.effect }
            .reduce { acc, effect -> acc + effect }

    return Triple(finalEffect, resultingReport, effectsList)
}

private fun Action.getRollResultEffects(stance: Stance, rollResult: RollResult): Pair<FacesReport, List<ActionEffect>> =
        when (stance) {
            RECKLESS -> recklessSide.getRollResultEffects(rollResult)
            CONSERVATIVE, NEUTRAL -> conservativeSide.getRollResultEffects(rollResult)
        }

private fun ActionSide.getRollResultEffects(rollResult: RollResult): Pair<FacesReport, List<ActionEffect>> =
        effects?.let { actionEffects ->
            val effectsList = actionEffects.getEffectsList()

            val resultingReport: HashMap<Face, Int> = hashMapOf()
            val activatedEffects = rollResult.report.flatMap { reportEntry ->
                val canBeActivatedEffects = effectsList.filter { it.face === reportEntry.key && it.faceCount <= reportEntry.value }

                val (activatedEffects, currentFaceCount) = canBeActivatedEffects.getActivatedEffectsAndRemoveUsedFaces(reportEntry.value)
                resultingReport[reportEntry.key] = currentFaceCount

                activatedEffects
            }

            Pair(resultingReport, activatedEffects)
        } ?: Pair(rollResult.report, listOf())

private fun List<ActionEffect>.getActivatedEffectsAndRemoveUsedFaces(faceCount: Int): Pair<List<ActionEffect>, Int> {
    var currentFaceCount = faceCount
    val activatedEffects = sortedByDescending { it.faceCount }
            .mapNotNull {
                if (currentFaceCount >= it.faceCount) {
                    currentFaceCount -= it.faceCount
                    it
                } else {
                    null
                }
            }
            .sortedBy { it.faceCount }

    return Pair(activatedEffects, currentFaceCount)
}
