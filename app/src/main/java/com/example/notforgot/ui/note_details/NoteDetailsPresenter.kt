package com.example.notforgot.ui.note_details

import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import com.example.notforgot.R
import com.example.notforgot.models.network.data.Task
import java.text.DateFormat
import java.util.*

class NoteDetailsPresenter : NoteDetailsContract.Presenter {

    private var view: NoteDetailsContract.View? = null
    private var model: NoteDetailsContract.Model? = NoteDetailsModel()

    override fun onEditButtonClick() {
        view?.gotoEdit()
    }

    override fun processTaskDetails(task: Task, resources: Resources) {
        view?.setTitle(task.title)
        view?.setDescription(task.description)
        view?.setDate(DateFormat.getDateInstance(DateFormat.FULL).format(Date(task.deadline * 1000)))
        if (task.done == 1){
            view?.setStatus(resources.getString(R.string.completed))
        }
        else{
            view?.setStatus(resources.getString(R.string.pending))
        }
        view?.setPriority(task.priority.name, Color.parseColor(task.priority.color))
    }

    override fun attachView(view: NoteDetailsContract.View, resources: Resources) {
        this.view = view
        this.view?.setToolbarTitle(resources.getString(R.string.note))
    }

    override fun onDestroyView() {
        this.view = null
    }

    override fun onDestroy() {
        Log.d("NoteDetailsPresenter", "Destroy")
        this.model = null
    }

    override fun onBackButtonClick() {
        view?.finish()
    }
}