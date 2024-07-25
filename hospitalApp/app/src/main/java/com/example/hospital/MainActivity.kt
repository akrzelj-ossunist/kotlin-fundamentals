package com.example.hospital

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var hospital: Hospital
    private lateinit var addDiagnosisLauncher: ActivityResultLauncher<Intent>
    private lateinit var patientContainer: LinearLayout

    private var selectedPatient: Patient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        hospital = Hospital()
        patientContainer = findViewById(R.id.patientContainer)

        val firstNameEditText: EditText = findViewById(R.id.firstName)
        val lastNameEditText: EditText = findViewById(R.id.lastName)
        val birthdayEditText: EditText = findViewById(R.id.birthday)
        val oibEditText: EditText = findViewById(R.id.oib)
        val mboEditText: EditText = findViewById(R.id.mbo)
        val addPatientButton: Button = findViewById(R.id.addPatientButton)

        addPatientButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val birthday = birthdayEditText.text.toString()
            val oib = oibEditText.text.toString()
            val mbo = mboEditText.text.toString()

            try {
                val patient = Patient(firstName, lastName, birthday, oib, mbo)
                hospital.addPatient(patient)
                updatePatientList()

                // Clear the input fields after adding a patient
                firstNameEditText.text.clear()
                lastNameEditText.text.clear()
                birthdayEditText.text.clear()
                oibEditText.text.clear()
                mboEditText.text.clear()
            } catch (e: IllegalArgumentException) {
                // Handle validation error
                Log.e("MainActivity", "Validation error: ${e.message}")
            }
        }

        addDiagnosisLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val diagnosis = result.data?.getStringExtra("diagnosis")
                selectedPatient?.let {
                    if (diagnosis != null) {
                        hospital.addDiagnosis(it, diagnosis)
                        updatePatientList()
                    }
                }
            }
        }

        updatePatientList()
    }

    private fun updatePatientList() {
        patientContainer.removeAllViews()
        for (status in hospital.statuses) {
            val patient = status.patient
            val patientView = layoutInflater.inflate(R.layout.patient_item, patientContainer, false)

            val patientTextView: TextView = patientView.findViewById(R.id.patientTextView)
            patientTextView.text = "${patient.firstName} ${patient.lastName}"

            val diagnosisButton: Button = patientView.findViewById(R.id.diagnosisButton)
            diagnosisButton.text = if (status.diagnosis == null) "Add Diagnosis" else "Remove Diagnosis"

            diagnosisButton.setOnClickListener {
                selectedPatient = patient
                if (status.diagnosis == null) {
                    val intent = Intent(this, AddDiagnosisActivity::class.java)
                    addDiagnosisLauncher.launch(intent)
                } else {
                    hospital.removeDiagnosis(patient)
                    updatePatientList()
                }
            }

            val removePatientButton: Button = patientView.findViewById(R.id.removePatientButton)
            removePatientButton.setOnClickListener {
                hospital.removePatient(patient)
                updatePatientList()
            }

            val diagnosisTextView: TextView = patientView.findViewById(R.id.diagnosisTextView)
            diagnosisTextView.text = status.diagnosis ?: ""

            patientContainer.addView(patientView)
        }
    }
}
