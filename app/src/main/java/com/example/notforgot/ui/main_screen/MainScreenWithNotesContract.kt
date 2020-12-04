package com.example.notforgot.ui.main_screen

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.notforgot.adapters.MainRecyclerAdapter
import com.example.notforgot.models.IBaseModel
import com.example.notforgot.models.IBasePresenter
import com.example.notforgot.models.IBaseView
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.models.network.data.Task

interface MainScreenWithNotesContract : IBaseView {
    interface View : IBaseView, MainRecyclerAdapter.ItemListener {
        fun logout()
        fun enableSynchronizationAnimation()
        fun disableSynchronizationAnimation()
        fun showEmptyScreen()
        fun stopRefreshAnimation()
        fun attachRecyclerAdapter(adapter: MainRecyclerAdapter)
        fun openTask(task: Task)
        fun gotoAddTask()
        override fun onNoteStateChanged(item: Task, position: Int)
        override fun onNoteClicked(item: Task, position: Int)
    }

    interface Presenter : IBasePresenter {
        fun onCreate(context: Context)
        fun attachView(view: View, context: Context, resources: android.content.res.Resources)
        fun onExitButtonClick(context: Context)
        fun updateTasksList(context: Context)
        fun onTaskClick(task: Task, position: Int)
        fun onAddTaskClick()
        fun onSwipeRefresh(context: Context)
        fun onSwipeDelete(context: Context, viewHolder: RecyclerView.ViewHolder)
        fun onTaskStateChange(context: Context, task: Task, position: Int)
        fun setupRecycler(context: Context, list: ArrayList<RecyclerObject>)
    }

    interface Model : IBaseModel {
        suspend fun synchronize(context: Context, callback: (result: Boolean) -> Unit)
        suspend fun getTasks(context: Context): ArrayList<RecyclerObject>
        fun deleteUserInfo(context: Context)
        suspend fun deleteTask(context: Context, position: Int)
        suspend fun changeTaskState(context: Context, task: Task, position: Int): String?
        fun checkTasksListEmptiness(): Boolean
    }
}