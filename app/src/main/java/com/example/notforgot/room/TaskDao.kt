package com.example.notforgot.room

import androidx.room.*
import com.example.notforgot.models.network.data.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(item: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: ArrayList<Task>)

    @Query("SELECT * FROM Task where title like :name")
    fun getTask(name: String): Task

    @Query("SELECT * FROM Task")
    fun getAllTasks(): List<Task>

    @Delete
    fun deleteTask(item: Task)

    @Query("DELETE FROM Task")
    fun deleteAllTasks()
}