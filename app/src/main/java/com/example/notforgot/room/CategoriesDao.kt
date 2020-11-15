package com.example.notforgot.room

import androidx.room.*
import com.example.notforgot.models.DataConverter
import com.example.notforgot.models.network.Category


@Dao
interface CategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Category>)

    @Query("SELECT * FROM Category where category_name like :name")
    fun getCategory(name: String): Category

    @Transaction
    @Query("SELECT * FROM Category")
    fun getCategoryWithItems(): List<Category>

    @Query("SELECT * FROM Category")
    fun getAllCategories(): List<Category>

    @Query("SELECT category_name FROM Category")
    fun getTitles(): List<String>

}
