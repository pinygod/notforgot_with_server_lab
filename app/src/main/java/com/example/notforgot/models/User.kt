package com.example.notforgot.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    indices = [Index(
        value =
        ["email"], unique = true
    )]
)
data class User(
    val email: String,
    val password: String,
    val name: String?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Int = 0
) : Serializable