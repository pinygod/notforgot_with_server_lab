package com.example.notforgot.room

import androidx.room.*
import com.example.notforgot.models.DataConverter
import com.example.notforgot.models.Note

@Dao
interface NoteItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: ArrayList<Note>)

    @Query("SELECT * FROM Note where title like :name")
    fun getItem(name: String): Note

    @Delete
    fun deleteItem(item: Note)
}