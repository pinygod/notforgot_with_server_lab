package com.example.notforgot.models.network.data

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity
data class Category(
    @PrimaryKey
    @ColumnInfo(name = "category_id")
    @SerializedName("id")
    val categoryId: Int,
    @ColumnInfo(name = "category_name")
    val name: String
)

class CategoryWithTasks(
    @Embedded
    var category: Category,
    @Relation(parentColumn = "category_id", entityColumn = "category_id")
    var items: List<Task>
)
