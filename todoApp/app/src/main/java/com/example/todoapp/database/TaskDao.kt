// TaskDao.kt
package com.example.todoapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    suspend fun createTask(task: Task)

    @Query("DELETE FROM tasks WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY taskId DESC")
    suspend fun getTasksByUserId(userId: Int): List<Task>

    @Query("SELECT * FROM tasks WHERE userId = :userId AND priority = :priority AND isCompleted = :isCompleted")
    suspend fun filterTasks(userId: Int, priority: Int, isCompleted: Boolean): List<Task>
}
