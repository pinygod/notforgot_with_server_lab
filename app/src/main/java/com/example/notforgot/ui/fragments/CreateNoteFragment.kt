package com.example.notforgot.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.notforgot.models.CategoryForm
import com.example.notforgot.models.InputValidation
import com.example.notforgot.models.network.*
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateNoteFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var note: Task? = null

    private var defaultCategory = "Select category"
    private var defaultPriority = "Select priority"
    private lateinit var categoryNames: ArrayList<String>
    private lateinit var priorityNames: ArrayList<String>
    private lateinit var categories: ArrayList<Category>
    private lateinit var priorities: ArrayList<Priority>
    private lateinit var settedDate: Date
    private var isDateSetted = false
    private lateinit var categorySpinnerAdapter: ArrayAdapter<String>
    private lateinit var prioritySpinnerAdapter: ArrayAdapter<String>
    private var isRedacting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            note = requireArguments().getSerializable("Note") as Task
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (note != null) {
            isRedacting = true
            name.setText(note!!.title)
            description.setText(note!!.description)
            defaultCategory = note!!.category.name
            defaultPriority = note!!.priority.name
            settedDate = Date(note!!.deadline * 1000)
            isDateSetted = true
            val currentDateString: String =
                DateFormat.getDateInstance(DateFormat.FULL).format(settedDate)
            date.setText(currentDateString)
        }
        symbolsCounter.text = (description.text.toString().length.toString() + "/120")
        categoryNames = ArrayList()
        priorityNames = ArrayList()
        initCategoriesList()
        initPrioritiesList()
        categorySpinnerAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_item, categoryNames
            )
        categorySpinner.adapter = categorySpinnerAdapter
        prioritySpinnerAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_item, priorityNames
            )
        prioritySpinner.adapter = prioritySpinnerAdapter

        description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                symbolsCounter.text = (s.length.toString() + "/120")
            }
        })

        calendarButton.setOnClickListener {
            openDatePicker()
        }

        date.setOnClickListener {
            openDatePicker()
        }

        noteDate.setOnClickListener {
            openDatePicker()
        }

        saveButton.setOnClickListener {
            savingAnimation.progress = 0f
            savingLayout.visibility = View.VISIBLE
            savingAnimation.playAnimation()
            if (InputValidation(requireContext()).isInputEditTextFilled(
                    name,
                    noteName,
                    "It is necessary"
                ) && InputValidation(requireContext()).isInputEditTextFilled(
                    description,
                    noteDescription,
                    "It is necessary"
                ) &&
                isDateSetted
            ) {
                val name = name.text.toString()
                val description = description.text.toString()

                if (isRedacting) {
                    val category =
                        categories.find { s -> s.name == categorySpinner.selectedItem.toString() }

                    val priority =
                        priorities.find { s -> s.name == prioritySpinner.selectedItem.toString() }

                    GlobalScope.launch(Dispatchers.Main) {
                        withContext(Dispatchers.IO) {
                            var error: String? = null
                            try {
                                ApiInteractions(Network.getInstance(), requireContext()).updateTask(
                                    note!!.taskId,
                                    TaskForm(
                                        name,
                                        description,
                                        0,
                                        (settedDate.time / 1000),
                                        category!!.categoryId,
                                        priority!!.id
                                    )
                                )
                            } catch (e: Exception) {
                                error = "Something went wrong..."
                            }
                            if (error == null) {
                                //TODO add this task into db
                                findNavController().popBackStack()
                            } else {
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                } else {
                    val categoryName = categorySpinner.selectedItem.toString()
                    val priorityName = prioritySpinner.selectedItem.toString()
                    if (categoryName != defaultCategory && priorityName != defaultPriority) {
                        val category =
                            categories.find { s -> s.name == categorySpinner.selectedItem.toString() }

                        val priority =
                            priorities.find { s -> s.name == prioritySpinner.selectedItem.toString() }

                        GlobalScope.launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                var error: String? = null
                                try {
                                    ApiInteractions(
                                        Network.getInstance(),
                                        requireContext()
                                    ).createTask(
                                        TaskForm(
                                            name,
                                            description,
                                            0,
                                            (settedDate.time / 1000),
                                            category!!.categoryId,
                                            priority!!.id
                                        )
                                    )
                                } catch (e: Exception) {
                                    error = "Something went wrong..."
                                }
                                if (error == null) {
                                    //TODO add this task into db
                                    findNavController().popBackStack()
                                } else {
                                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    } else {
                        savingLayout.visibility = View.INVISIBLE
                        savingAnimation.pauseAnimation()
                        Toast.makeText(
                            requireContext(),
                            "Not all necessary fields are completed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                savingLayout.visibility = View.INVISIBLE
                savingAnimation.pauseAnimation()
                Toast.makeText(
                    requireContext(),
                    "Not all necessary fields are completed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        addCategoryButton.setOnClickListener {
            val view: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_category_dialog, null)
            val builder =
                AlertDialog.Builder(requireContext()).setView(view)

            val dialog = builder.show()


            view.positiveButton.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    var error: String? = null
                    val category = withContext(Dispatchers.IO) {
                        try {
                            ApiInteractions(Network.getInstance(), requireContext()).createCategory(
                                CategoryForm(view.categoryName.text.toString())
                            )
                        } catch (e: Exception) {
                            error = "Something went wrong..."
                        }
                    }
                    if (error == null) {
                        categories.add(category as Category)
                        categoryNames.add(category.name)
                        categorySpinnerAdapter.notifyDataSetChanged()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            view.negativeButton.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isRedacting)
            requireActivity().setTitle(R.string.redact_note)
        else
            requireActivity().setTitle(R.string.create_note)
    }

    private fun openDatePicker() {
        val datePicker = DatePickerFragment()
        datePicker.setTargetFragment(this, 0);
        datePicker.show(requireFragmentManager(), "date picker");
    }

    private fun initPrioritiesList() {
        priorityNames = ArrayList()
        priorityNames.add(defaultPriority)
        GlobalScope.launch(Dispatchers.Main) {
            priorities = withContext(Dispatchers.IO) {
                ApiInteractions(
                    Network.getInstance(),
                    requireContext()
                ).getAllPriorities() as ArrayList<Priority>
            }
            priorities.forEach {
                priorityNames.add(it.name)
            }
            prioritySpinnerAdapter.notifyDataSetChanged()
        }
    }

    private fun initCategoriesList() {
        categoryNames = ArrayList()
        categoryNames.add(defaultCategory)
        GlobalScope.launch(Dispatchers.Main) {
            categories = withContext(Dispatchers.IO) {
                ApiInteractions(
                    Network.getInstance(),
                    requireContext()
                ).getAllCategories() as ArrayList<Category>
            }
            categories.forEach {
                categoryNames.add(it.name)
            }
            categorySpinnerAdapter.notifyDataSetChanged()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day
        val currentDateString: String =
            DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        date.setText(currentDateString)
        settedDate = calendar.time
        isDateSetted = true
    }


}