package com.example.notforgot.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notforgot.R
import com.example.notforgot.models.Note
import kotlinx.android.synthetic.main.activity_note_details.*
import java.text.DateFormat

class NoteDetailsActivity : AppCompatActivity() {

    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        note = intent.getSerializableExtra("Note") as Note
        description.text = note.description
        if (note.checkBoxCondition){
            status.text = resources.getString(R.string.completed)
        }
        else{
            status.text = resources.getString(R.string.pending)
        }
        category.text = note.categoryTitle
        date.text = DateFormat.getDateInstance(DateFormat.FULL).format(note.date)
        priority.text = note.priority
        noteTitle.text = note.title

        redactNoteButton.setOnClickListener{
            val myIntent = Intent(this, CreateNoteActivity::class.java)
            myIntent.putExtra("Note", note)
            startActivity(myIntent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}