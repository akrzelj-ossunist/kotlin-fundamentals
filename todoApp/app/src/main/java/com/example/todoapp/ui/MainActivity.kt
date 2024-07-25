package com.example.todoapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.AppDatabase
import com.example.todoapp.database.Task
import com.example.todoapp.ui.adapter.TaskAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var taskDescriptionEditText: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var addTaskButton: Button
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var filterPrioritySpinner: Spinner
    private lateinit var filterStatusRadioGroup: RadioGroup

    private var userId: Int = 0
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getIntExtra("userId", 0)
        if (userId == 0) {
            finish()
        }

        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText)
        prioritySpinner = findViewById(R.id.prioritySpinner)
        addTaskButton = findViewById(R.id.addTaskButton)
        taskRecyclerView = findViewById(R.id.taskRecyclerView)
        filterPrioritySpinner = findViewById(R.id.filterPrioritySpinner)
        filterStatusRadioGroup = findViewById(R.id.filterStatusRadioGroup)

        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(mutableListOf())
        taskRecyclerView.adapter = taskAdapter

        loadTasks()

        addTaskButton.setOnClickListener {
            val description = taskDescriptionEditText.text.toString()
            val priority = prioritySpinner.selectedItem.toString().toInt()

            if (description.isNotEmpty()) {
                val task = Task(userId = userId, description = description, priority = priority)
                GlobalScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(applicationContext).taskDao().createTask(task)
                    loadTasks()
                }
            } else {
                Toast.makeText(applicationContext, "Please enter a task description", Toast.LENGTH_SHORT).show()
            }
        }

        filterStatusRadioGroup.setOnCheckedChangeListener { _, _ -> filterTasks() }
        //filterPrioritySpinner.setOnItemSelectedListener { filterTasks() }
    }

    private fun loadTasks() {
        GlobalScope.launch(Dispatchers.IO) {
            val tasks = AppDatabase.getDatabase(applicationContext).taskDao().getTasksByUserId(userId)
            runOnUiThread {
                taskAdapter.updateTasks(tasks)
            }
        }
    }

    private fun filterTasks() {
        val priority = filterPrioritySpinner.selectedItem.toString().toInt()
        val isCompleted = when (filterStatusRadioGroup.checkedRadioButtonId) {
            R.id.radioCompleted -> true
            R.id.radioNotCompleted -> false
            else -> null
        }

        GlobalScope.launch(Dispatchers.IO) {
            val tasks = isCompleted?.let {
                AppDatabase.getDatabase(applicationContext).taskDao().filterTasks(userId, priority, it)
            }
            runOnUiThread {
                if (tasks != null) {
                    taskAdapter.updateTasks(tasks)
                }
            }
        }
    }
}
