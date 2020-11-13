package com.example.notforgot.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notforgot.models.Category
import com.example.notforgot.models.DataConverter
import com.example.notforgot.models.Note
import com.example.notforgot.models.User

@Database(version = 1, entities = [Category::class, Note::class, User::class])
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun get(application: Context): AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    "user_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun getCategoryDao(): CategoriesDao
    abstract fun getItemDao(): NoteItemDao
    abstract fun getUserDao(): UsersDao
}