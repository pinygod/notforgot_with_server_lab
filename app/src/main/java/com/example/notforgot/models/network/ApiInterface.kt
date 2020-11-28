package com.example.notforgot.models.network

import com.example.notforgot.models.network.data.*
import retrofit2.http.*

interface ApiInterface {

    @GET("priorities")
    suspend fun getAllPriorities(): List<Priority>

    @GET("categories")
    suspend fun getAllCategories(): List<Category>

    @GET("tasks")
    suspend fun getAllTasks(): List<Task>
    //@Header("Authorization") token: String

    @POST("categories")
    suspend fun createCategory(@Body form: CategoryForm): Category

    @POST("tasks")
    suspend fun createTask(@Body form: TaskForm): Task

    @POST("register")
    suspend fun registerUser(@Body form: UserRegistrationForm): User

    @POST("login")
    suspend fun loginUser(@Body form: UserLoginForm): Token

    @PATCH("tasks/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body form: TaskForm): Task

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int)
}