package com.example.sqliteactappdev.repository

import com.example.sqliteactappdev.database.UserDatabaseHelper
import com.example.sqliteactappdev.model.User

interface UserRepository {
    suspend fun insertUser(username: String, password: String): Result<Long>
    suspend fun getUser(username: String, password: String): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun getAllUsers(): List<User>
}

class UserRepositoryImpl(
    private val dbHelper: UserDatabaseHelper
) : UserRepository {
    
    override suspend fun insertUser(username: String, password: String): Result<Long> {
        return try {
            val result = dbHelper.insertUser(username, password)
            if (result != -1L) {
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to insert user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(username: String, password: String): User? {
        return dbHelper.getUser(username, password)
    }

    override suspend fun getUserByUsername(username: String): User? {
        return dbHelper.getUserByUsername(username)
    }

    override suspend fun getAllUsers(): List<User> {
        return dbHelper.getAllUsers()
    }
}

