package com.example.notforgot.models.network.data

import androidx.room.*
import com.example.notforgot.models.network.data.Category
import com.example.notforgot.models.network.data.Priority
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Task(
    val title: String,
    val description: String,
    var done: Int,
    val deadline: Long,
    @Embedded
    val category: Category,
    @Embedded
    val priority: Priority,
    var created: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    @SerializedName("id")
    val taskId: Int = 0
) : Serializable
