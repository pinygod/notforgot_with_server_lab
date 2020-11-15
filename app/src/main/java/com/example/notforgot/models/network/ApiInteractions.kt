package com.example.notforgot.models.network

import android.content.Context
import com.example.notforgot.models.*

class ApiInteractions(private val api: ApiInterface, private val context: Context) {

    suspend fun getAllPriorities(): List<Priority> {
        return api.getAllPriorities("Bearer " + PreferenceUtils.getUserToken(
            context
        )
        )
    }

    suspend fun getAllCategories(): List<Category> {
        return api.getAllCategories("Bearer " + PreferenceUtils.getUserToken(
            context
        )
        )
    }

    suspend fun getAllTasks(): List<Task> {
        return api.getAllTasks("Bearer " + PreferenceUtils.getUserToken(
            context
        )
        )
    }

    suspend fun createCategory(form: CategoryForm): Category {
        return api.createCategory("Bearer " + PreferenceUtils.getUserToken(
            context
        ), form)
    }

    suspend fun createTask(form: TaskForm): Task {
        return api.createTask("Bearer " + PreferenceUtils.getUserToken(
            context
        ), form)
    }

    suspend fun registerUser(form: UserRegistrationForm): User {
        return api.registerUser(form)
    }

    suspend fun loginUser(form: UserLoginForm): Token {
        return api.loginUser(form)
    }

    suspend fun updateTask(id: Int, form: TaskForm): Task {
        return api.updateTask("Bearer " + PreferenceUtils.getUserToken(
            context
        ), id, form)
    }

    suspend fun deleteTask(id: Int) {
        return api.deleteTask("Bearer " + PreferenceUtils.getUserToken(
            context
        ), id)
    }
}