package com.nicolas.diceroller.roll

import com.nicolas.models.dice.Face.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FacesReportTest {
    @Test
    fun should_convert_list_of_faces_to_report() {
        val faces = listOf(SUCCESS, BOON)
        val report = facesToFacesReport(faces)

        assertThat(report.keys.size).isEqualTo(2)
        assertThat(report[SUCCESS]).isEqualTo(1)
        assertThat(report[BOON]).isEqualTo(1)
    }

    @Test
    fun should_convert_complex_list_of_faces_to_report() {
        val faces = listOf(SUCCESS, BOON, DELAY, DELAY, SUCCESS, SIGMAR)
        val report = facesToFacesReport(faces)

        assertThat(report.keys.size).isEqualTo(4)
        assertThat(report[SUCCESS]).isEqualTo(2)
        assertThat(report[BOON]).isEqualTo(1)
        assertThat(report[DELAY]).isEqualTo(2)
        assertThat(report[SIGMAR]).isEqualTo(1)
    }

    @Test
    fun should_merge_simple_report_alone() {
        val faces = listOf(SUCCESS)
        val report = facesToFacesReport(faces)

        assertThat(report.keys.size).isEqualTo(1)
        assertThat(report[SUCCESS]).isEqualTo(1)

        val mergedReport = mergeReports(listOf(report))
        assertThat(mergedReport.keys.size).isEqualTo(1)
        assertThat(mergedReport[SUCCESS]).isEqualTo(1)
        assertThat(mergedReport).isEqualToComparingFieldByField(report)
    }
}