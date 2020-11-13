package com.example.notforgot.room

import androidx.room.*
import com.example.notforgot.models.DataConverter
import com.example.notforgot.models.User

@Dao
interface UsersDao {

    @Query("SELECT * FROM User")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM User where name like :name")
    fun getAllUsers(name: String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(users: List<User>)

    @Query("SELECT * FROM User where User.email = :email AND User.password = :password")
    fun checkCredentials(email: String, password: String): User
}