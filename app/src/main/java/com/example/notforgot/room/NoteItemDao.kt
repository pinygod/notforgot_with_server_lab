package com.example.notforgot.room

import androidx.room.*
import com.example.notforgot.models.DataConverter
import com.example.notforgot.models.network.Task

@Dao
interface NoteItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: ArrayList<Task>)

    @Query("SELECT * FROM Task where title like :name")
    fun getItem(name: String): Task

    @Delete
    fun deleteItem(item: Task)
}