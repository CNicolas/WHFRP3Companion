package com.nicolas.dicelauncher.launch

import com.nicolas.dicelauncher.dices.Dice
import com.nicolas.dicelauncher.dices.Face
import com.nicolas.dicelauncher.dices.Face.*
import com.nicolas.dicelauncher.dices.impl.bad.ChallengeDice
import com.nicolas.dicelauncher.dices.impl.good.*
import com.nicolas.whfrp3database.entities.hand.Hand
import warhammer.dicelauncher.dices.impl.bad.MisfortuneDice
import com.nicolas.dicelauncher.statistics.LaunchStatistics

fun Hand.launch(): LaunchResult = launchHand(this)
fun Hand.launchForStatistics(count: Int): LaunchStatistics = launchHandForStatistics(this, count)

fun launchHand(Hand: Hand): LaunchResult {
    val faces = simplifyFaces(Hand.launchForFaces())
    return LaunchResult(faces)
}

fun launchHandForStatistics(Hand: Hand, count: Int): LaunchStatistics {
    val launchResults = mutableListOf<LaunchResult>()

    (0 until count).forEach { launchResults.add(launchHand(Hand)) }

    return LaunchStatistics(launchResults)
}

internal fun simplifyFaces(faces: List<Face>): List<Face> {
    val report = facesToFacesReport(faces)

    return facesReportToFaces(removeOpposites(report))
}

private fun Hand.launchForFaces(): List<Face> {
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
