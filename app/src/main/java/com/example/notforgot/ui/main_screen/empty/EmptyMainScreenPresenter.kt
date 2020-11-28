package com.example.notforgot.ui.main_screen.empty

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.example.notforgot.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmptyMainScreenPresenter :
    EmptyMainScreenContract.Presenter {

    private var view: EmptyMainScreenContract.View? = null
    private var model: EmptyMainScreenContract.Model? = EmptyMainScreenModel()

    override fun attachView(
        view: EmptyMainScreenContract.View,
        context: Context,
        resources: Resources
    ) {
        this.view = view
        this.view?.setToolbarTitle(resources.getString(R.string.app_name))
    }

    override fun onDestroyView() {
        this.view = null
    }

    override fun onDestroy() {
        Log.d("EmptyPresenter", "Destroy")
        this.model = null
    }

    override fun onAddTaskClick() {
        view?.gotoAddTask()
    }

    override fun onExitButtonClick(context: Context) {
        model!!.deleteUserInfo(context)
        view?.logout()
    }

    override fun checkForTasksExistence(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            when (withContext(Dispatchers.IO) {
                model!!.checkForTasksExistence(context)
            }) {
                true -> this@EmptyMainScreenPresenter.view?.showTasks()
            }
        }
    }

    override fun onSwipeRefresh(context: Context) {
        checkForTasksExistence(context)
    }
}