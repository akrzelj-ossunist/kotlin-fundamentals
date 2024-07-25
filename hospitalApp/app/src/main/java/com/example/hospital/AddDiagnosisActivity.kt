package com.example.hospital

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class AddDiagnosisActivity : AppCompatActivity() {
    private val diagnoses = listOf("Flu", "Cold", "COVID-19", "Bronchitis", "Pneumonia")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diagnosis)

        val listView: ListView = findViewById(R.id.diagnosisListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, diagnoses)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedDiagnosis = diagnoses[position]
            val resultIntent = intent
            resultIntent.putExtra("diagnosis", selectedDiagnosis)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val cancelButton: Button = findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            finish()
        }
    }
}
