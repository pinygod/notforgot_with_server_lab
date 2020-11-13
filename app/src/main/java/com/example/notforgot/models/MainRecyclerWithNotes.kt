package com.example.notforgot.models

import androidx.room.*
import java.io.Serializable
import java.util.*

@Entity(indices = [Index(value =
["title"], unique = true)])
class Category(
    var title: String,
    @Embedded
    var user: User,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Serializable

@Entity
class Note(
    var title: String,
    var description: String,
    var priority: String,
    var checkBoxCondition: Boolean = false,
    var color: Int = 0,
    @ColumnInfo(name = "category_id")
    var categoryId: Int,
    @ColumnInfo(name = "category_title")
    var categoryTitle: String,
    var date: Date,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Serializable

class CategoryWithItems(
    @Embedded
    var category: Category,
    @Relation(parentColumn = "id", entityColumn = "category_id")
    var items: List<Note>
) : Serializable

