package com.example.notforgot.room

import androidx.room.*
import com.example.notforgot.models.network.data.Priority

@Dao
interface PriorityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPriority(priority: Priority)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Priority>)

    @Query("SELECT * FROM Priority where priority_name like :name")
    fun getPriority(name: String): Priority

    @Query("SELECT * FROM Priority")
    fun getAllPriorities(): List<Priority>

    @Query("SELECT priority_name FROM Priority")
    fun getTitles(): List<String>

    @Query("DELETE FROM Priority")
    fun deleteAllPriorities()

}