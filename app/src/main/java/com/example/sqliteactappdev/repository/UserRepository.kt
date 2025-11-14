package com.example.sqliteactappdev.repository

import com.example.sqliteactappdev.database.UserDatabaseHelper
import com.example.sqliteactappdev.model.User

interface UserRepository {
    suspend fun insertUser(username: String, password: String, role: String): Result<Long>
    suspend fun getUser(username: String, password: String): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun getAllUsers(): List<User>
    suspend fun updateUser(id: Int, username: String, password: String, role: String): Result<Int>
    suspend fun deleteUser(id: Int): Result<Int>
}

class UserRepositoryImpl(
    private val dbHelper: UserDatabaseHelper
) : UserRepository {
    
    override suspend fun insertUser(username: String, password: String, role: String): Result<Long> {
        return try {
            val result = dbHelper.insertUser(username, password, role)
            if (result != -1L) {
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to insert user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(id: Int, username: String, password: String, role: String): Result<Int> {
        return try {
            val result = dbHelper.updateUser(id, username, password, role)
            if (result > 0) {
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to update user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(id: Int): Result<Int> {
        return try {
            val result = dbHelper.deleteUser(id)
            if (result > 0) {
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to delete user"))
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

