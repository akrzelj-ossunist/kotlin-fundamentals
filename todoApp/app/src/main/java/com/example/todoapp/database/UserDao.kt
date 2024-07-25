// UserDao.kt
package com.example.todoapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun loginUser(username: String, password: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun deleteUser(userId: Int)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}