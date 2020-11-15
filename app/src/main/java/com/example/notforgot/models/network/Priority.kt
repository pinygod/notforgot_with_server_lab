package com.example.notforgot.models.network

import androidx.room.ColumnInfo

data class Priority(
    val id: Int,
    @ColumnInfo(name = "priority_name")
    val name: String,
    val color: String
)