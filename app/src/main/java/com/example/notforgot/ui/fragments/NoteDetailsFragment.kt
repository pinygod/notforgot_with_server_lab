package com.example.notforgot.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R
import com.example.notforgot.models.Note
import kotlinx.android.synthetic.main.activity_note_details.*
import java.text.DateFormat

class NoteDetailsFragment : Fragment() {

    private lateinit var note : Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = arguments?.getSerializable("Note") as Note
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            findNavController().popBackStack()
            findNavController().navigate(R.id.createNoteFragment, bundleOf("Note" to note))
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.note)
    }


}