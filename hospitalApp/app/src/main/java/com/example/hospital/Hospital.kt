package com.example.hospital

import java.text.SimpleDateFormat
import java.util.*

data class Patient(
    val firstName: String,
    val lastName: String,
    val birthday: String,
    val oib: String,
    val mbo: String
) {
    init {
        require(oib.length == 11) { "OIB must be 11 characters long" }
        require(mbo.length == 9) { "MBO must be 9 characters long" }
    }
}

data class Status(
    val patient: Patient,
    var diagnosis: String? = null,
    var receiptDate: String? = null,
    var dischargedDate: String? = null
)

class Hospital {
    val statuses = mutableListOf<Status>()

    fun addPatient(patient: Patient) {
        val status = Status(patient)
        statuses.add(status)
    }

    fun removePatient(patient: Patient) {
        statuses.removeAll { it.patient == patient }
    }

    fun addDiagnosis(patient: Patient, diagnosis: String) {
        val status = statuses.find { it.patient == patient }
        status?.let {
            it.diagnosis = diagnosis
            it.receiptDate = getCurrentDate()
        }
    }

    fun removeDiagnosis(patient: Patient) {
        val status = statuses.find { it.patient == patient }
        status?.let {
            it.diagnosis = null
            it.dischargedDate = getCurrentDate()
        }
    }

    fun listPatients(): List<String> {
        return statuses.map { it.patient.firstName }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
