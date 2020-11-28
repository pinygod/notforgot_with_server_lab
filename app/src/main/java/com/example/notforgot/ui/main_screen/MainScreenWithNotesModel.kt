package com.example.notforgot.ui.main_screen

import android.content.Context
import android.util.Log
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.models.network.Api
import com.example.notforgot.models.network.data.Category
import com.example.notforgot.models.network.data.CategoryWithTasks
import com.example.notforgot.models.network.data.Task
import com.example.notforgot.models.network.data.TaskForm
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.ui.MainActivity
import com.example.notforgot.utils.Constants
import com.example.notforgot.utils.PreferenceUtils
import kotlinx.coroutines.*
import java.lang.Exception

class MainScreenWithNotesModel : MainScreenWithNotesContract.Model {

    private var recyclerObjectsList: ArrayList<RecyclerObject> = ArrayList()
    private var isSynchronized = false

    override suspend fun getTasks(context: Context): ArrayList<RecyclerObject> {
        if (!getValuesFromServer(context)) {
            getValuesFromDB(context)
        }
        return recyclerObjectsList
    }

    override fun deleteUserInfo(context: Context) {
        PreferenceUtils.deleteUserToken(context)
        AppDatabase.get(context).getTaskDao().deleteAllTasks()
        AppDatabase.get(context).getCategoryDao().deleteAllCategories()
        AppDatabase.get(context).getPriorityDao().deleteAllPriorities()
    }

    override suspend fun deleteTask(
        context: Context,
        position: Int
    ) {
        if (recyclerObjectsList[position].type == Constants.TYPE_NOTE) {
            try {
                Api.getInstance(context).deleteTask(
                    (recyclerObjectsList[position].item as Task).taskId
                )
                AppDatabase.get(context).getTaskDao()
                    .deleteTask(recyclerObjectsList[position].item as Task)
            } catch (e: Exception) {
                (recyclerObjectsList[position].item as Task).created = Constants.TO_BE_DELETED
                AppDatabase.get(context).getTaskDao()
                    .insertTask(recyclerObjectsList[position].item as Task)
                Log.d("http error", e.message!!)
            }
        }

        if (recyclerObjectsList.size > position + 1) {
            if (recyclerObjectsList[position - 1].type == Constants.TYPE_TITLE && recyclerObjectsList[position + 1].type == Constants.TYPE_TITLE) {
                recyclerObjectsList.removeAt(position)
                recyclerObjectsList.removeAt(position - 1)
            } else {
                recyclerObjectsList.removeAt(position)
            }
        } else {
            if (recyclerObjectsList[position - 1].type == Constants.TYPE_TITLE) {
                recyclerObjectsList.removeAt(position)
                recyclerObjectsList.removeAt(position - 1)
            } else {
                recyclerObjectsList.removeAt(position)
            }
        }
    }

    private suspend fun getValuesFromServer(context: Context): Boolean {
        var success = true
        val categoriesList =
            try {
                Api.getInstance(context).getAllCategories()
            } catch (e: Exception) {
                success = false
                Log.d("http error", e.message!!)
            }

        val tasksList =
            try {
                Api.getInstance(context).getAllTasks()
            } catch (e: Exception) {
                success = false
                Log.d("http error", e.message!!)
            }

        if (success) {
            recyclerObjectsList.clear()
            (categoriesList as ArrayList<Category>).forEach { category ->
                val tasksOfCategory: List<Task> =
                    (tasksList as ArrayList<Task>).filter { s -> s.category == category }
                if (tasksOfCategory.isNotEmpty()) {
                    recyclerObjectsList.add(RecyclerObject(Constants.TYPE_TITLE, category))
                    tasksOfCategory.forEach {
                        recyclerObjectsList.add(RecyclerObject(Constants.TYPE_NOTE, it))
                    }
                }
            }
        }

        return success
    }

    private fun getValuesFromDB(context: Context) {
        recyclerObjectsList.clear()
        val categoriesList = AppDatabase.get(context).getCategoryDao()
            .getCategoryWithTasks() as ArrayList<CategoryWithTasks>

        categoriesList.forEach { category ->
            val tasksOfCategory =
                category.items.filter { s -> s.created != Constants.TO_BE_DELETED }
            if (tasksOfCategory.isNotEmpty()) {
                recyclerObjectsList.add(RecyclerObject(Constants.TYPE_TITLE, category.category))
                tasksOfCategory.forEach {
                    recyclerObjectsList.add(RecyclerObject(Constants.TYPE_NOTE, it))
                }
            }
        }
    }

    override suspend fun changeTaskState(context: Context, task: Task, position: Int): String? {
        var error: String? = null
        val newItem =
            try {
                Api.getInstance(context).updateTask(
                    task.taskId,
                    TaskForm(
                        task.title,
                        task.description,
                        if (task.done == 1)
                            0
                        else
                            1,
                        task.deadline,
                        task.category.categoryId,
                        task.priority.priorityId
                    )
                )
            } catch (e: Exception) {
                error = "Unable to change state now."
            }


        if (error == null) {
            task.done = (newItem as Task).done
            AppDatabase.get(context).getTaskDao().insertTask(newItem)
        }

        return error
    }

    override fun checkTasksListEmptiness(): Boolean {
        return recyclerObjectsList.isEmpty()
    }

    override suspend fun synchronize(context: Context) {
        isSynchronized = false

        while (!isSynchronized) {
            withContext(Dispatchers.Main) {
                MainActivity.enableSynchronizationAnimation()
            }
            var error = false

            val api = Api.getInstance(context)
            val localValues = AppDatabase.get(context).getTaskDao().getAllTasks()
            val newTasks = localValues.filter { s -> s.created == Constants.TO_BE_ADDED }
            val tasksToUpload = newTasks.filter { s -> s.taskId == Constants.TO_BE_ADDED }
            val tasksToUpdate = newTasks.filter { s -> s.taskId != Constants.TO_BE_ADDED }
            val tasksToDelete = localValues.filter { s -> s.created == Constants.TO_BE_DELETED }
            tasksToDelete.forEach {
                try {
                    api.deleteTask(it.taskId)
                } catch (e: Exception) {
                    error = true
                    Log.d(
                        "Synchronization",
                        "Error while deleting server val: " + it.taskId
                    )
                }
            }
            tasksToUpload.forEach {
                try {
                    api.createTask(
                        TaskForm(
                            it.title,
                            it.description,
                            it.done,
                            it.deadline,
                            it.category.categoryId,
                            it.priority.priorityId
                        )
                    )
                } catch (e: Exception) {
                    error = true
                    Log.d(
                        "Synchronization",
                        "Error while uploading task: " + it.taskId
                    )
                }
            }
            tasksToUpdate.forEach {
                try {
                    api.updateTask(
                        it.taskId,
                        TaskForm(
                            it.title,
                            it.description,
                            it.done,
                            it.deadline,
                            it.category.categoryId,
                            it.priority.priorityId
                        )
                    )
                } catch (e: Exception) {
                    error = true
                    Log.d(
                        "Synchronization",
                        "Error while updating task: " + it.taskId
                    )
                }
            }
            if (!error) {
                try {
                    val updatedTasks = api.getAllTasks()
                    AppDatabase.get(context).getTaskDao().deleteAllTasks()
                    AppDatabase.get(context).getTaskDao()
                        .insertAll(updatedTasks as ArrayList<Task>)
                    isSynchronized = true
                    Log.d(
                        "Synchronization",
                        "Successfully synchronized"
                    )
                } catch (e: Exception) {
                    error = true
                    Log.d("Synchronization", "Error while fetching tasks")
                }
            }
            withContext(Dispatchers.Main) {
                MainActivity.disableSynchronizationAnimation()
            }
            if (error) {
                Log.d("Synchronization", "Sleep for 15s")
                delay(15000)
            }
        }

    }
}