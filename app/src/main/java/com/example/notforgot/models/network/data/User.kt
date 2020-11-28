package com.example.notforgot.models.network.data

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
    val name: String,
    @PrimaryKey
    val id: Int = 0,
    val api_token: String
) : Serializable