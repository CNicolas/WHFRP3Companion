package com.nicolas.dicelauncher.statistics

import com.nicolas.dicelauncher.dices.Face.*
import com.nicolas.dicelauncher.launch.FacesReport
import com.nicolas.dicelauncher.launch.LaunchResult
import com.nicolas.dicelauncher.launch.mergeReports

data class LaunchStatistics(private val launchResults: List<LaunchResult>) {
    val launchCount = launchResults.size
    val successfulLaunchCount = launchResults.count { it.isSuccessful }
    val totalResultReport = mergeReports(launchResults.map { it.report })
    val successfulPercentage = (100 * successfulLaunchCount) / launchCount.toDouble()

    val totalVoid = totalResultReport[VOID] ?: 0
    val totalSuccess = totalResultReport[SUCCESS] ?: 0
    val totalBoon = totalResultReport[BOON] ?: 0
    val totalDelay = totalResultReport[DELAY] ?: 0
    val totalSigmar = totalResultReport[SIGMAR] ?: 0
    val totalFailure = totalResultReport[FAILURE] ?: 0
    val totalBane = totalResultReport[BANE] ?: 0
    val totalExhaustion = totalResultReport[EXHAUSTION] ?: 0
    val totalChaos = totalResultReport[CHAOS] ?: 0

    val averageVoid: Double = totalVoid.toDouble() / launchCount
    val averageSuccess: Double = totalSuccess.toDouble() / launchCount
    val averageBoon: Double = totalBoon.toDouble() / launchCount
    val averageDelay: Double = totalDelay.toDouble() / launchCount
    val averageSigmar: Double = totalSigmar.toDouble() / launchCount
    val averageFailure: Double = totalFailure.toDouble() / launchCount
    val averageBane: Double = totalBane.toDouble() / launchCount
    val averageExhaustion: Double = totalExhaustion.toDouble() / launchCount
    val averageChaos: Double = totalChaos.toDouble() / launchCount

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