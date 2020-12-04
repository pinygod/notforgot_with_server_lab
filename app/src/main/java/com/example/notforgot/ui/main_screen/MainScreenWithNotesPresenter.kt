package com.example.notforgot.ui.main_screen

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.notforgot.R
import com.example.notforgot.adapters.MainRecyclerAdapter
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.models.network.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainScreenWithNotesPresenter :
    MainScreenWithNotesContract.Presenter {

    private var view: MainScreenWithNotesContract.View? = null
    private var model: MainScreenWithNotesContract.Model? = MainScreenWithNotesModel()
    private var adapter: MainRecyclerAdapter? = null

    override fun attachView(
        view: MainScreenWithNotesContract.View,
        context: Context,
        resources: Resources
    ) {
        this.view = view
        this.view?.setToolbarTitle(resources.getString(R.string.app_name))
        GlobalScope.launch(Dispatchers.IO) {
            val tasks = model!!.getTasks(context)
            if (tasks.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    setupRecycler(context, tasks)
                }
            } else
                this@MainScreenWithNotesPresenter.view?.showEmptyScreen()
        }
    }

    override fun onDestroyView() {
        this.view = null
    }

    override fun onCreate(context: Context) {
        synchronize(context)
    }

    private fun synchronize(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            model!!.synchronize(context) { result ->
                if (result) {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.enableSynchronizationAnimation()
                    }
                } else {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.disableSynchronizationAnimation()
                    }
                }
            }
        }
    }

    private suspend fun getTasksAndLoadItToAdapter(context: Context) {
        val tasks = model!!.getTasks(context)
        if (tasks.isNotEmpty()) {
            withContext(Dispatchers.Main) {
                adapter?.updateList(tasks)
            }
        } else
            view?.showEmptyScreen()
    }

    override fun onDestroy() {
        Log.d("MainPresenter", "Destroy")
        this.model = null
    }

    override fun onExitButtonClick(context: Context) {
        model!!.deleteUserInfo(context)
        view?.logout()
    }

    override fun updateTasksList(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            getTasksAndLoadItToAdapter(context)
        }
    }

    override fun onTaskClick(task: Task, position: Int) {
        view?.openTask(task)
    }

    override fun onAddTaskClick() {
        view?.gotoAddTask()
    }

    override fun onSwipeRefresh(context: Context) {
        updateTasksList(context)
        view?.stopRefreshAnimation()
    }

    override fun onSwipeDelete(context: Context, viewHolder: RecyclerView.ViewHolder) {
        GlobalScope.launch(Dispatchers.IO) {
            model!!.deleteTask(context, viewHolder.adapterPosition)
            updateTasksList(context)
        }
    }

    override fun onTaskStateChange(context: Context, task: Task, position: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            model!!.changeTaskState(context, task, position)
        }
    }

    override fun setupRecycler(context: Context, list: ArrayList<RecyclerObject>) {
        adapter = MainRecyclerAdapter(list, context, view!!)
        view?.attachRecyclerAdapter(adapter!!)
    }
}