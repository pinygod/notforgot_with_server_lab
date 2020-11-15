package com.example.notforgot.models.network

import androidx.room.*
import com.example.notforgot.models.network.Category
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Task(
    @PrimaryKey
    @ColumnInfo(name = "task_id")
    @SerializedName("id")
    val taskId: Int,
    val title: String,
    val description: String,
    var done: Int,
    val deadline: Long,
    @Embedded
    val category: Category,
    @Embedded
    val priority: Priority,
    val created: Int
) : Serializable
