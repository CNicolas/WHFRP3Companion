package com.nicolas.diceroller.roll

import com.nicolas.diceroller.dices.Face

typealias FacesReport = Map<Face, Int>

internal fun facesToFacesReport(faces: List<Face>): FacesReport {
    val report = hashMapOf<Face, Int>()

    faces.distinct()
            .map { face -> report.put(face, faces.count { it == face }) }

    return report
}

internal fun facesReportToFaces(facesReport: FacesReport): List<Face> {
    return facesReport.flatMap { entry ->
        (0 until entry.value).map { entry.key }
    }
}

fun mergeReports(reports: List<FacesReport>): FacesReport =
        reports.reduce { finalReport, rollResultReport -> mergeTwoReports(finalReport, rollResultReport) }

private fun mergeTwoReports(report1: FacesReport, report2: FacesReport): FacesReport =
        report1.mergeReduce(report2) { a, b -> a + b }

private fun <K, V> Map<K, V>.mergeReduce(other: Map<K, V>, reduce: (V, V) -> V = { _, b -> b }): Map<K, V> {
    val result = LinkedHashMap<K, V>(this.size + other.size)
    result.putAll(this)
    other.forEach { (key, value) ->
        result[key] = result[key]?.let { reduce(value, it) } ?: value
    }
    return result
}
