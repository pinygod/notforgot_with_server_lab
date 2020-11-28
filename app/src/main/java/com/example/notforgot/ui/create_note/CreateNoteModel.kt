package com.example.notforgot.ui.create_note

import android.content.Context
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.notforgot.models.network.Api
import com.example.notforgot.models.network.data.*
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.ui.fragments.DatePickerFragment
import com.example.notforgot.utils.Constants
import com.example.notforgot.utils.InputValidation
import java.lang.Exception
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateNoteModel : CreateNoteContract.Model {

    private var validator = InputValidation()

    private var defaultCategory = "Select category"
    private var defaultPriority = "Select priority"
    private lateinit var categoryNames: ArrayList<String>
    private lateinit var priorityNames: ArrayList<String>
    private lateinit var categories: ArrayList<Category>
    private lateinit var priorities: ArrayList<Priority>
    private var date: Date? = null

    override suspend fun getCategories(
        defaultCategory: String?,
        context: Context
    ): ArrayList<String> {
        if (defaultCategory != null) {
            this.defaultCategory = defaultCategory
        }
        categoryNames = ArrayList()
        categoryNames.add(this.defaultCategory)
        var error = false
        categories =
            try {
                Api.getInstance(context).getAllCategories()
            } catch (e: Exception) {
                error = true
                AppDatabase.get(context).getCategoryDao().getAllCategories()
            }
                    as ArrayList<Category>

        if (!error) {
            AppDatabase.get(context).getCategoryDao().deleteAllCategories()
            AppDatabase.get(context).getCategoryDao().insertAll(categories)
        }
        categories.forEach {
            categoryNames.add(it.name)
        }
        return categoryNames
    }

    override suspend fun getPriorities(
        defaultPriority: String?,
        context: Context
    ): ArrayList<String> {
        if (defaultPriority != null) {
            this.defaultPriority = defaultPriority
        }
        priorityNames = ArrayList()
        priorityNames.add(this.defaultPriority)
        var error = false
        priorities =
            try {
                Api.getInstance(context).getAllPriorities()
            } catch (e: Exception) {
                error = true
                AppDatabase.get(context).getPriorityDao().getAllPriorities()
            }
                    as ArrayList<Priority>

        if (!error) {
            AppDatabase.get(context).getPriorityDao().deleteAllPriorities()
            AppDatabase.get(context).getPriorityDao().insertAll(priorities)
        }
        priorities.forEach {
            priorityNames.add(it.name)
        }
        return priorityNames
    }

    override suspend fun tryToSave(
        note: Task?,
        title: String,
        description: String,
        categoryTitle: String,
        priorityTitle: String,
        context: Context
    ): String? {

        val newId: Int
        var newDone = 0

        if (note != null) {
            newId = note.taskId
            note.created = Constants.TO_BE_DELETED
            newDone = note.done
        } else
            newId = Constants.TO_BE_ADDED

        val category =
            categories.find { s -> s.name == categoryTitle }!! // can not return null cuz titles were loaded from these lists
        val priority = priorities.find { s -> s.name == priorityTitle }!! //

        var error: String? = null
        val form = TaskForm(
            title,
            description,
            newDone,
            date!!.time / 1000, // date can not be null at this moment
            category.categoryId,
            priority.priorityId
        )
        val task =
            try {
                if (note == null) {
                    Api.getInstance(context).createTask(form)
                } else {
                    Api.getInstance(context).updateTask(note.taskId, form)
                }

            } catch (e: Exception) {
                error = "Something went wrong on server..."
            }

        if (error == null) {
            AppDatabase.get(context).getTaskDao().insertTask(task as Task)
            //MainActivity.addTask(task)
            return error
        } else {

            val newTask = Task(
                newId,
                form.title,
                form.description,
                form.done,
                form.deadline,
                Category(form.categoryId, "Will be shown later"),
                Priority(form.priorityId, "Will be shown later", "#CCCCCC"),
                Constants.TO_BE_ADDED
            )
            AppDatabase.get(context).getTaskDao().insertTask(newTask)
            //MainActivity.addTask(newTask)
            //MainActivity.synchronize()
            return error
        }
    }

    override fun validateTitle(name: String): Boolean {
        return validator.isInputEditTextFilled(name)
    }

    override fun validateDescription(description: String): Boolean {
        return validator.isInputEditTextFilled(description)
    }

    override fun validateCategory(category: String): Boolean {
        return (category != defaultCategory)
    }

    override fun validatePriority(priority: String): Boolean {
        return (priority != defaultPriority)
    }

    override fun validateDate(task: Task?): Boolean {
        if (date == null) {
            if (task == null) {
                return false
            }
            date = Date(task.deadline * 1000)
        }
        return true
    }

    override fun openDateDialog(fragment: Fragment, fm: FragmentManager) {
        val datePicker = DatePickerFragment()
        datePicker.setTargetFragment(fragment, 0)
        datePicker.show(fm, "date picker")
    }

    override fun setDate(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        val currentDateString: String =
            DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        date = calendar.time
        return currentDateString
    }

    override suspend fun addCategory(categoryName: String, context: Context): String? {
        var error: String? = null
        val category =
            try {
                Api.getInstance(context).createCategory(
                    CategoryForm(
                        categoryName
                    )
                )
            } catch (e: Exception) {
                error = "Something went wrong..."
            }

        if (error == null) {
            categories.add(category as Category)
            AppDatabase.get(context).getCategoryDao().insertCategory(category)
            categoryNames.add(category.name)
        }
        return error
    }
}