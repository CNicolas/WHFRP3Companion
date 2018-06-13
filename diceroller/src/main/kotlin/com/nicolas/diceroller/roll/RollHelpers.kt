package com.nicolas.diceroller.roll

import com.nicolas.diceroller.statistics.RollStatistics
import com.nicolas.models.dice.Dice
import com.nicolas.models.dice.Face
import com.nicolas.models.dice.Face.*
import com.nicolas.models.dice.impl.bad.ChallengeDice
import com.nicolas.models.dice.impl.bad.MisfortuneDice
import com.nicolas.models.dice.impl.good.*
import com.nicolas.models.hand.Hand

fun Hand.roll(): RollResult = rollHand(this)
fun Hand.rollForStatistics(count: Int): RollStatistics = rollHandForStatistics(this, count)

fun rollHand(Hand: Hand): RollResult {
    val faces = simplifyFaces(Hand.rollForFaces())
    return RollResult(faces)
}

fun rollHandForStatistics(Hand: Hand, count: Int): RollStatistics {
    val rollResults = mutableListOf<RollResult>()

    (0 until count).forEach { rollResults.add(rollHand(Hand)) }

    return RollStatistics(rollResults)
}

internal fun simplifyFaces(faces: List<Face>): List<Face> {
    val report = facesToFacesReport(faces)

    return facesReportToFaces(removeOpposites(report))
}

private fun Hand.rollForFaces(): List<Face> {
    val pool = createPool(this)

    return pool.flatMap { it.roll() }
}

private fun createPool(Hand: Hand): List<Dice> {
    val pool = mutableListOf<Dice>()

    (0 until Hand.characteristicDicesCount).forEach { pool.add(CharacteristicDice()) }
    (0 until Hand.expertiseDicesCount).forEach { pool.add(ExpertiseDice()) }
    (0 until Hand.fortuneDicesCount).forEach { pool.add(FortuneDice()) }
    (0 until Hand.conservativeDicesCount).forEach { pool.add(ConservativeDice()) }
    (0 until Hand.recklessDicesCount).forEach { pool.add(RecklessDice()) }
    (0 until Hand.challengeDicesCount).forEach { pool.add(ChallengeDice()) }
    (0 until Hand.misfortuneDicesCount).forEach { pool.add(MisfortuneDice()) }

    return pool.toList()
}

private val opposingFace: Map<Face, Face> = hashMapOf(
        SUCCESS to FAILURE,
        FAILURE to SUCCESS,
        BOON to BANE,
        BANE to BOON)

private fun removeOpposites(report: FacesReport): FacesReport {
    val mutableReport = HashMap(report)

    mutableReport.entries.forEach { entry ->
        val (face, faceCount) = entry

        run {
            if (faceCount > 0) {
                val opposite = opposingFace[face]
                if (opposite != null) {
                    when {
                        mutableReport[opposite] == null -> {
                        }
                        faceCount == mutableReport[opposite] -> {
                            mutableReport[face] = 0
                            mutableReport[opposite] = 0
                        }
                        faceCount > mutableReport[opposite]!! -> {
                            mutableReport[face] = report[face]!! - report[opposite]!!
                            mutableReport[opposite] = 0
                        }
                        faceCount < mutableReport[opposite]!! -> {
                            mutableReport[face] = 0
                            mutableReport[opposite] = report[opposite]!! - report[face]!!
                        }
                    }
                }
            }
        }
    }

    return mutableReport
}
