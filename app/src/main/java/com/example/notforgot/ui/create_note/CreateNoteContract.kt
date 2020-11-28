package com.example.notforgot.ui.create_note

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.notforgot.models.IBaseModel
import com.example.notforgot.models.IBasePresenter
import com.example.notforgot.models.IBaseView
import com.example.notforgot.models.network.data.Task
import kotlin.collections.ArrayList

interface CreateNoteContract {

    interface View : IBaseView {
        fun startSavingAnimation()
        fun stopSavingAnimation()
        fun showTitleError(error: String)
        fun showDescriptionError(error: String)
        fun attachCategoriesSpinnerAdapter(adapter: ArrayAdapter<String>)
        fun attachPrioritiesSpinnerAdapter(adapter: ArrayAdapter<String>)
        fun dismissAddCategoryDialog()
        fun finishWhenRedacting()
        fun setTitle(title: String)
        fun setDescription(description: String)
        fun setDate(date: String)
    }

    interface Presenter : IBasePresenter {
        fun attachView(
            view: View,
            context: Context,
            resources: android.content.res.Resources,
            task: Task?
        )

        fun setupAdapters(context: Context)
        fun onSaveButtonClick(
            title: String, description: String, category: String, priority: String, context: Context
        )

        fun onDateClicked(fragment: Fragment, fm: FragmentManager)
        fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
        fun onBackButtonClick()
        fun onCategoryAddClick(categoryName: String, context: Context)
    }

    interface Model : IBaseModel {
        suspend fun getCategories(defaultCategory: String?, context: Context): ArrayList<String>
        suspend fun getPriorities(defaultPriority: String?, context: Context): ArrayList<String>
        suspend fun tryToSave(
            note: Task?,
            title: String,
            description: String,
            categoryTitle: String,
            priorityTitle: String,
            context: Context
        ): String?

        fun validateTitle(name: String): Boolean
        fun validateDescription(description: String): Boolean
        fun validateCategory(category: String): Boolean
        fun validatePriority(priority: String): Boolean
        fun validateDate(task: Task?): Boolean
        fun openDateDialog(fragment: Fragment, fm: FragmentManager)
        fun setDate(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int): String
        suspend fun addCategory(categoryName: String, context: Context): String?
    }
}