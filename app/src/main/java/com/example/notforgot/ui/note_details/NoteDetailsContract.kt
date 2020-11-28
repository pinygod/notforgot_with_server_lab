package com.example.notforgot.ui.note_details

import com.example.notforgot.models.IBaseModel
import com.example.notforgot.models.IBasePresenter
import com.example.notforgot.models.IBaseView
import com.example.notforgot.models.network.data.Task

interface NoteDetailsContract {
    interface View : IBaseView {
        fun gotoEdit()
        fun setTitle(title: String)
        fun setDescription(description: String)
        fun setDate(date: String)
        fun setCategory(category: String)
        fun setPriority(priority: String, color: Int)
        fun setStatus(status: String)
    }

    interface Presenter :IBasePresenter {
        fun attachView(view: View, resources: android.content.res.Resources)
        fun onBackButtonClick()
        fun onEditButtonClick()
        fun processTaskDetails(task: Task, resources: android.content.res.Resources)
    }

    interface Model :IBaseModel {
    }

}