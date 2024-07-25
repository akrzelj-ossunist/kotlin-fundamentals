// Task.kt
package com.example.todoapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val userId: Int,
    val description: String,
    val priority: Int,
    var isCompleted: Boolean = false
)