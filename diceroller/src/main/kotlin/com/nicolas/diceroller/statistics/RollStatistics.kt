package com.nicolas.diceroller.statistics

import com.nicolas.diceroller.roll.FacesReport
import com.nicolas.diceroller.roll.RollResult
import com.nicolas.diceroller.roll.mergeReports
import com.nicolas.models.dice.Face.*

data class RollStatistics(private val rollResults: List<RollResult>) {
    val rollCount = rollResults.size
    val successfulRollCount = rollResults.count { it.isSuccessful }
    val totalResultReport = mergeReports(rollResults.map { it.report })
    val successfulPercentage = (100 * successfulRollCount) / rollCount.toDouble()

    val totalVoid = totalResultReport[VOID] ?: 0
    val totalSuccess = totalResultReport[SUCCESS] ?: 0
    val totalBoon = totalResultReport[BOON] ?: 0
    val totalDelay = totalResultReport[DELAY] ?: 0
    val totalSigmar = totalResultReport[SIGMAR] ?: 0
    val totalFailure = totalResultReport[FAILURE] ?: 0
    val totalBane = totalResultReport[BANE] ?: 0
    val totalExhaustion = totalResultReport[EXHAUSTION] ?: 0
    val totalChaos = totalResultReport[CHAOS] ?: 0

    val averageVoid: Double = totalVoid.toDouble() / rollCount
    val averageSuccess: Double = totalSuccess.toDouble() / rollCount
    val averageBoon: Double = totalBoon.toDouble() / rollCount
    val averageDelay: Double = totalDelay.toDouble() / rollCount
    val averageSigmar: Double = totalSigmar.toDouble() / rollCount
    val averageFailure: Double = totalFailure.toDouble() / rollCount
    val averageBane: Double = totalBane.toDouble() / rollCount
    val averageExhaustion: Double = totalExhaustion.toDouble() / rollCount
    val averageChaos: Double = totalChaos.toDouble() / rollCount

    val averageResult: FacesReport = hashMapOf(
            // VOID to Math.round(averageVoid).toInt(),
            SUCCESS to Math.round(averageSuccess).toInt(),
            BOON to Math.round(averageBoon).toInt(),
            DELAY to Math.round(averageDelay).toInt(),
            SIGMAR to Math.round(averageSigmar).toInt(),
            FAILURE to Math.round(averageFailure).toInt(),
            BANE to Math.round(averageBane).toInt(),
            EXHAUSTION to Math.round(averageExhaustion).toInt(),
            CHAOS to Math.round(averageChaos).toInt()
    ).filterValues { it > 0 }
}