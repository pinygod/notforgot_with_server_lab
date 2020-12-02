package com.example.notforgot.ui.main_screen.empty

import android.content.Context
import android.util.Log
import com.example.notforgot.models.network.Api
import com.example.notforgot.models.network.data.Category
import com.example.notforgot.models.network.data.CategoryWithTasks
import com.example.notforgot.models.network.data.Task
import com.example.notforgot.room.AppDatabase

import com.example.notforgot.utils.Constants
import com.example.notforgot.utils.PreferenceUtils
import kotlinx.coroutines.*

class EmptyMainScreenModel : EmptyMainScreenContract.Model {

    override suspend fun checkForTasksExistence(context: Context): Boolean {
        return if (checkForTasksExistenceOnServer(context))
            true
        else getValuesFromDB(context)
    }

    override fun deleteUserInfo(context: Context) {
        PreferenceUtils.deleteUserToken(context)
        AppDatabase.get(context).clearAllTables()
    }


    private suspend fun checkForTasksExistenceOnServer(context: Context): Boolean {
        var error = false
        val categoriesList = withContext(Dispatchers.IO) {
            try {
                Api.getInstance(context).getAllCategories()
            } catch (e: Exception) {
                error = true
                Log.d("Fetching tasks", e.message!!)
                return@withContext !error
            }
        }
        val tasksList = withContext(Dispatchers.IO) {
            try {
                Api.getInstance(context).getAllTasks()
            } catch (e: Exception) {
                error = true
                Log.d("Fetching tasks", e.message!!)
                return@withContext !error
            }
        }

        if (!error) {
            (categoriesList as ArrayList<Category>).forEach {
                val tasksOfCategory: List<Task> =
                    (tasksList as ArrayList<Task>).filter { s -> s.category == it }
                if (tasksOfCategory.isNotEmpty())
                    return true // at least 1 task exists -> need to show tasks
            }
        }

        return !error
    }


    private fun getValuesFromDB(context: Context): Boolean {
        val listCategories = AppDatabase.get(context).getCategoryDao()
            .getCategoryWithTasks() as ArrayList<CategoryWithTasks>

        listCategories.forEach {
            val categoryTasks = it.items.filter { s -> s.created != Constants.TO_BE_DELETED }
            if (categoryTasks.isNotEmpty()) {
                return true // at least 1 task exists -> need to show tasks
            }
        }
        return false
    }


}