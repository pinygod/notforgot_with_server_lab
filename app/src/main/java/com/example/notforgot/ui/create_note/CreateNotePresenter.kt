package com.example.notforgot.ui.create_note

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.notforgot.R
import com.example.notforgot.models.network.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat

class CreateNotePresenter :
    CreateNoteContract.Presenter {

    private var view: CreateNoteContract.View? = null
    private var model: CreateNoteContract.Model? = CreateNoteModel()
    private var task: Task? = null

    private lateinit var categorySpinnerAdapter: ArrayAdapter<String>
    private lateinit var prioritySpinnerAdapter: ArrayAdapter<String>

    override fun onSaveButtonClick(
        title: String,
        description: String,
        category: String,
        priority: String,
        context: Context
    ) {

        view!!.startSavingAnimation()

        if (!model!!.validateTitle(title)) {
            view!!.stopSavingAnimation()
            view!!.showTitleError("Incorrect title")
            return
        }
        if (!model!!.validateDescription(description)) {
            view!!.stopSavingAnimation()
            view!!.showDescriptionError("Incorrect description")
            return
        }
        if (!model!!.validateDate(task)) {
            view!!.stopSavingAnimation()
            view!!.showError("Incorrect date")
            return
        }
        if (task == null) {
            if (!model!!.validateCategory(category)) {
                view!!.stopSavingAnimation()
                view!!.showError("Incorrect category")
                return
            }
            if (!model!!.validatePriority(priority)) {
                view!!.stopSavingAnimation()
                view!!.showError("Incorrect priority")
                return
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            val error = withContext(Dispatchers.IO) {
                model!!.tryToSave(
                    task,
                    title,
                    description,
                    category,
                    priority,
                    context
                )
            }
            if (error != null) {
                view!!.showError(error)
            }
            view!!.finish()
        }

    }

    override fun onDateClicked(fragment: Fragment, fm: FragmentManager) {
        model!!.openDateDialog(fragment, fm)
    }

    override fun setupAdapters(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            val categoryNames = withContext(Dispatchers.IO) {
                model!!.getCategories(task?.category?.name, context)
            }
            categorySpinnerAdapter =
                ArrayAdapter(
                    context,
                    R.layout.spinner_item, categoryNames
                )
            view!!.attachCategoriesSpinnerAdapter(categorySpinnerAdapter)

            val priorityNames = withContext(Dispatchers.IO) {
                model!!.getPriorities(task?.priority?.name, context)
            }
            prioritySpinnerAdapter =
                ArrayAdapter(
                    context,
                    R.layout.spinner_item, priorityNames
                )
            view!!.attachPrioritiesSpinnerAdapter(prioritySpinnerAdapter)
        }
    }

    override fun attachView(
        view: CreateNoteContract.View,
        context: Context,
        resources: Resources,
        task: Task?
    ) {
        this.view = view
        this.task = task

        if (task != null) {
            this.view?.setToolbarTitle(resources.getString(R.string.redact_note))
            this.view?.setTitle(task.title)
            this.view?.setDescription(task.description)
            this.view?.setDate(
                DateFormat.getDateInstance(DateFormat.FULL).format(task.deadline * 1000)
            )
        } else
            this.view?.setToolbarTitle(resources.getString(R.string.create_note))

        setupAdapters(context)
    }

    override fun onDestroyView() {
        this.view = null
    }

    override fun onDestroy() {
        Log.d("CreateNotePresenter", "Destroy")
        this.model = null
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.view!!.setDate(model!!.setDate(view, year, month, dayOfMonth))
    }

    override fun onBackButtonClick() {
        if (task != null) {
            view?.finishWhenRedacting()
        } else
            view?.finish()
    }

    override fun onCategoryAddClick(categoryName: String, context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            val error = withContext(Dispatchers.IO) {
                model!!.addCategory(categoryName, context)
            }
            if (error == null) {
                categorySpinnerAdapter.notifyDataSetChanged()
                view!!.dismissAddCategoryDialog()
            } else {
                view!!.showError(error)
            }
        }

    }
}