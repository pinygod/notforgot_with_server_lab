package com.example.notforgot.ui.main_screen.empty

import android.content.Context
import com.example.notforgot.models.IBaseModel
import com.example.notforgot.models.IBasePresenter
import com.example.notforgot.models.IBaseView

interface EmptyMainScreenContract {
    interface View : IBaseView {
        fun logout()
        fun showTasks()
        fun gotoAddTask()
    }

    interface Presenter : IBasePresenter {
        fun attachView(view: View, context: Context, resources: android.content.res.Resources)
        fun onAddTaskClick()
        fun onExitButtonClick(context: Context)
        fun checkForTasksExistence(context: Context)
        fun onSwipeRefresh(context: Context)
    }

    interface Model : IBaseModel {
        suspend fun checkForTasksExistence(context: Context): Boolean
        fun deleteUserInfo(context: Context)
    }
}