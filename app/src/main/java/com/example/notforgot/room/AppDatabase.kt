package com.example.notforgot.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notforgot.models.DataConverter
import com.example.notforgot.models.network.data.Category
import com.example.notforgot.models.network.data.Priority
import com.example.notforgot.models.network.data.Task
import com.example.notforgot.models.network.data.User

@Database(version = 1, entities = [Category::class, Task::class, Priority::class])
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
    abstract fun getTaskDao(): TaskDao
    abstract fun getPriorityDao(): PriorityDao
}