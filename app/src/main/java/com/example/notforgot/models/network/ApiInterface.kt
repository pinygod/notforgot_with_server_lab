package com.example.notforgot.models.network

import com.example.notforgot.models.*
import retrofit2.http.*

interface ApiInterface {

    @GET("priorities")
    suspend fun getAllPriorities(@Header("Authorization") token: String): List<Priority>

    @GET("categories")
    suspend fun getAllCategories(@Header("Authorization") token: String): List<Category>

    @GET("tasks")
    suspend fun getAllTasks(@Header("Authorization") token: String): List<Task>

    @POST("categories")
    suspend fun createCategory(@Header("Authorization") token: String, @Body form: CategoryForm): Category

    @POST("tasks")
    suspend fun createTask(@Header("Authorization") token: String, @Body form: TaskForm): Task

    @POST("register")
    suspend fun registerUser(@Body form: UserRegistrationForm): User

    @POST("login")
    suspend fun loginUser(@Body form: UserLoginForm): Token

    @PATCH("tasks/{id}")
    suspend fun updateTask(@Header("Authorization") token: String, @Path("id") id: Int, @Body form: TaskForm): Task

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Header("Authorization") token: String, @Path("id") id: Int)
}