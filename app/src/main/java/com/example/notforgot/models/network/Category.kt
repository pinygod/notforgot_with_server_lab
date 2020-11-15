package com.example.notforgot.models.network

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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