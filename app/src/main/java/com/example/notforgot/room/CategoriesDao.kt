package com.example.notforgot.room

import androidx.room.*
import com.example.notforgot.models.CategoryWithItems
import com.example.notforgot.models.Category
import com.example.notforgot.models.DataConverter


@Dao
interface CategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Category>)

    @Query("SELECT * FROM Category where title like :name AND user_id = :userId")
    fun getCategory(name: String, userId: Int): CategoryWithItems

    @Transaction
    @Query("SELECT * FROM Category where user_id = :userId")
    fun getCategoryWithItems(userId: Int): List<CategoryWithItems>

    @Query("SELECT * FROM Category where user_id = :userId")
    fun getAllCategories(userId: Int): List<Category>

    @Query("SELECT title FROM Category where user_id = :userId")
    fun getTitles(userId: Int): List<String>

}
