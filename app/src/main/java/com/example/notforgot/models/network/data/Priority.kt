package com.example.notforgot.models.network.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Priority(
    @PrimaryKey
    @ColumnInfo(name = "priority_id")
    @SerializedName("id")
    val priorityId: Int,
    @ColumnInfo(name = "priority_name")
    val name: String,
    val color: String
)