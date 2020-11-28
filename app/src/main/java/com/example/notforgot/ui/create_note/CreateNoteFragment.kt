package com.example.notforgot.ui.create_note

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R
import com.example.notforgot.models.network.data.Task
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import kotlinx.android.synthetic.main.secondary_toolbar.*

class CreateNoteFragment : Fragment(), CreateNoteContract.View, DatePickerDialog.OnDateSetListener {

    private lateinit var presenter: CreateNoteContract.Presenter
    private var task: Task? = null

    private lateinit var addCategoryDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            task = CreateNoteFragmentArgs.fromBundle(
                it
            ).task
        }
        presenter = CreateNotePresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this, requireContext(), resources, task)

        calendarButton.setOnClickListener {
            presenter.onDateClicked(this, requireFragmentManager())
        }

        date.setOnClickListener {
            presenter.onDateClicked(this, requireFragmentManager())
        }

        noteDate.setOnClickListener {
            presenter.onDateClicked(this, requireFragmentManager())
        }

        backButton.setOnClickListener {
            presenter.onBackButtonClick()
        }

        saveButton.setOnClickListener {
            presenter.onSaveButtonClick(
                name.text.toString(),
                description.text.toString(),
                categorySpinner.selectedItem.toString(),
                prioritySpinner.selectedItem.toString(),
                requireContext()
            )
        }

        addCategoryButton.setOnClickListener {
            val view: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_category_dialog, null)
            val builder =
                AlertDialog.Builder(requireContext()).setView(view)

            val dialog = builder.show()
            addCategoryDialog = dialog

            view.positiveButton.setOnClickListener {
                presenter.onCategoryAddClick(view.categoryName.text.toString(), requireContext())
            }
            view.negativeButton.setOnClickListener {
                addCategoryDialog.dismiss()
            }
        }
    }

    override fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun showTitleError(error: String) {
        noteName.error = error
    }

    override fun showDescriptionError(error: String) {
        description.error = error
    }

    override fun attachCategoriesSpinnerAdapter(adapter: ArrayAdapter<String>) {
        categorySpinner.adapter = adapter
    }

    override fun attachPrioritiesSpinnerAdapter(adapter: ArrayAdapter<String>) {
        prioritySpinner.adapter = adapter
    }

    override fun finish() {
        findNavController().popBackStack()
    }

    override fun finishWhenRedacting() {
        findNavController().popBackStack()
        findNavController().popBackStack()
    }

    override fun setTitle(title: String) {
        this.name.setText(title)
    }

    override fun setDescription(description: String) {
        this.description.setText(description)
    }

    override fun dismissAddCategoryDialog() {
        addCategoryDialog.dismiss()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun setToolbarTitle(title: String) {
        this.toolbarTitle.text = title
    }

    override fun startSavingAnimation() {
        savingAnimation.progress = 0f
        savingLayout.visibility = View.VISIBLE
        savingAnimation.playAnimation()
    }

    override fun stopSavingAnimation() {
        savingLayout.visibility = View.INVISIBLE
        savingAnimation.pauseAnimation()
    }

    override fun setDate(date: String) {
        this.date.setText(date)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        presenter.onDateSet(view, year, month, dayOfMonth)
    }

}