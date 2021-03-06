package com.example.notforgot.models.network.data

import com.google.gson.annotations.SerializedName

data class TaskForm(
    val title: String,
    val description: String,
    var done: Int,
    val deadline: Long,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("priority_id")
    val priorityId: Int
)