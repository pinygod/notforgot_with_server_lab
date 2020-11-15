package com.example.notforgot.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R
import com.example.notforgot.models.network.Task
import kotlinx.android.synthetic.main.fragment_note_details.*
import java.text.DateFormat
import java.util.*

class NoteDetailsFragment : Fragment() {

    private lateinit var note : Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = arguments?.getSerializable("Note") as Task
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        description.text = note.description
        if (note.done == 1){
            status.text = resources.getString(R.string.completed)
        }
        else{
            status.text = resources.getString(R.string.pending)
        }
        category.text = note.category.name
        date.text = DateFormat.getDateInstance(DateFormat.FULL).format(Date(note.deadline * 1000))
        priority.text = note.priority.name
        priorityLabel.setCardBackgroundColor(Color.parseColor(note.priority.color))
        noteTitle.text = note.title

        redactNoteButton.setOnClickListener{
            findNavController().popBackStack()
            findNavController().navigate(R.id.createNoteFragment, bundleOf("Note" to note))
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.note)
    }


}