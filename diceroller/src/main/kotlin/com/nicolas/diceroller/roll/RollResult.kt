package com.nicolas.diceroller.roll

import com.nicolas.diceroller.dices.Face
import com.nicolas.diceroller.dices.Face.SUCCESS

data class RollResult(val faces: List<Face>) {
    val report: FacesReport = facesToFacesReport(faces)
    val isSuccessful: Boolean = faces.contains(SUCCESS)

    private val successfulString: String
        get() = if (isSuccessful) "OK" else "KO"

    override fun toString(): String {
        return "$successfulString $report"
    }

}