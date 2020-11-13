package com.example.notforgot.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.notforgot.ui.fragments.DatePickerFragment
import com.example.notforgot.R
import com.example.notforgot.models.Category
import com.example.notforgot.models.InputValidation
import com.example.notforgot.models.Note
import com.example.notforgot.room.AppDatabase
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreateNoteActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private var defaultCategory = "Select category"
    private var defaultPriority = "Select priority"
    private lateinit var categoriesList: ArrayList<String>
    private lateinit var prioritiesList: ArrayList<String>
    private lateinit var note: Note
    private lateinit var settedDate: Date
    private var isDateSetted = false
    private lateinit var categorySpinnerAdapter: ArrayAdapter<String>
    private lateinit var prioritySpinnerAdapter: ArrayAdapter<String>
    private var colors = listOf<Int>(
        R.color.cardBlueColor,
        R.color.cardRedColor,
        R.color.cardGreenColor,
        R.color.cardYellowColor
    )
    private var isRedacting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)
        if (intent.hasExtra("Note")) {
            isRedacting = true
            note = intent.getSerializableExtra("Note") as Note
            supportActionBar!!.title = getString(R.string.redact_note)
            name.setText(note.title)
            description.setText(note.description)
            defaultCategory = note.categoryTitle
            defaultPriority = note.priority
            settedDate = note.date
            isDateSetted = true
            val currentDateString: String =
                DateFormat.getDateInstance(DateFormat.FULL).format(settedDate)
            date.setText(currentDateString)
        }
        symbolsCounter.text = (description.text.toString().length.toString() + "/120")
        initCategoriesList()
        initPrioritiesList()
        categorySpinnerAdapter = ArrayAdapter(this,
            R.layout.spinner_item, categoriesList)
        categorySpinner.adapter = categorySpinnerAdapter
        prioritySpinnerAdapter = ArrayAdapter(this,
            R.layout.spinner_item, prioritiesList)
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
            if (InputValidation(this).isInputEditTextFilled(
                    name,
                    noteName,
                    "It is necessary"
                ) && InputValidation(this).isInputEditTextFilled(
                    description,
                    noteDescription,
                    "It is necessary"
                ) &&
                isDateSetted
            ) {
                val name = name.text.toString()
                val description = description.text.toString()
                Collections.shuffle(colors)
                if (isRedacting) {
                    val category = AppDatabase.get(application).getCategoryDao().getCategory(
                        categorySpinner.selectedItem.toString(),
                        MainActivity.getUser().userId
                    )
                    val priority = prioritySpinner.selectedItem.toString()
                    AppDatabase.get(application).getItemDao().insertItem(
                        Note(
                            name,
                            description,
                            priority,
                            false,
                            colors[0],
                            category.category.id,
                            category.category.title,
                            settedDate,
                            note.id
                        )
                    )
                    finish()
                } else {
                    val categoryName = categorySpinner.selectedItem.toString()
                    val priority = prioritySpinner.selectedItem.toString()
                    if (categoryName != defaultCategory && priority != defaultPriority) {
                        val category = AppDatabase.get(application).getCategoryDao().getCategory(
                            categoryName,
                            MainActivity.getUser().userId
                        )
                        AppDatabase.get(application).getItemDao().insertItem(
                            Note(
                                name,
                                description,
                                priority,
                                false,
                                colors[0],
                                category.category.id,
                                category.category.title,
                                settedDate
                            )
                        )
                        Toast.makeText(this,
                            R.string.succesfull_save, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        savingLayout.visibility = View.INVISIBLE
                        savingAnimation.pauseAnimation()
                        Toast.makeText(this, "Not all necessary fields are completed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                savingLayout.visibility = View.INVISIBLE
                savingAnimation.pauseAnimation()
                Toast.makeText(this, "Not all necessary fields are completed", Toast.LENGTH_SHORT).show()
            }
        }

        addCategoryButton.setOnClickListener {
            val view: View =
                LayoutInflater.from(this).inflate(R.layout.add_category_dialog, null)
            val builder =
                AlertDialog.Builder(this).setView(view)

            val dialog = builder.show()

            view.positiveButton.setOnClickListener {
                val tempCategory = Category(
                    view.categoryName.text.toString(),
                    MainActivity.getUser()
                )
                var isNew = true
                categoriesList.forEach {
                    if (it == tempCategory.title) {
                        isNew = false
                    }
                }
                if (isNew) {
                    AppDatabase.get(application).getCategoryDao().insertCategory(
                        tempCategory
                    )
                    categoriesList.add(tempCategory.title)
                    categorySpinnerAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
                else{
                    Toast.makeText(this, "Category with the same  is already exists", Toast.LENGTH_SHORT).show()
                }

            }
            view.negativeButton.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun openDatePicker() {
        val datePicker = DatePickerFragment()
        datePicker.show(supportFragmentManager, "date picker")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initPrioritiesList() {
        prioritiesList = ArrayList()
        prioritiesList.add(defaultPriority)
        prioritiesList.addAll(resources.getStringArray(R.array.priorities))
    }

    private fun initCategoriesList() {
        categoriesList = ArrayList()
        categoriesList.add(defaultCategory)
        categoriesList.addAll(
            AppDatabase.get(application).getCategoryDao()
                .getTitles(MainActivity.getUser().userId)
        )
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