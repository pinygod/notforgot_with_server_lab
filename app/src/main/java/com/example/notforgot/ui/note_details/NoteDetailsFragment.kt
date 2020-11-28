package com.example.notforgot.ui.note_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R
import com.example.notforgot.models.network.data.Task
import kotlinx.android.synthetic.main.fragment_note_details.*
import kotlinx.android.synthetic.main.secondary_toolbar.*

class NoteDetailsFragment : Fragment(), NoteDetailsContract.View {

    private lateinit var presenter: NoteDetailsContract.Presenter
    private lateinit var note : Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = NoteDetailsFragmentArgs.fromBundle(it).task
        }
        presenter = NoteDetailsPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this, resources)
        presenter.processTaskDetails(note, resources)

        backButton.setOnClickListener {
            presenter.onBackButtonClick()
        }

        redactNoteButton.setOnClickListener{
            presenter.onEditButtonClick()
        }

    }

    override fun onDestroyView() {
        presenter.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun setToolbarTitle(title: String) {
        this.toolbarTitle.text = title
    }

    override fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun gotoEdit() {
        val action =
            NoteDetailsFragmentDirections.actionNoteDetailsFragmentToCreateNoteFragment(
                note
            )
        findNavController().navigate(action)
    }

    override fun finish() {
        findNavController().popBackStack()
    }

    override fun setTitle(title: String) {
        this.noteTitle.text = title
    }

    override fun setDescription(description: String) {
        this.description.text = description
    }

    override fun setDate(date: String) {
        this.date.text = date
    }

    override fun setCategory(category: String) {
        this.category.text = category
    }

    override fun setPriority(priority: String, color: Int) {
        this.priority.text = priority
        this.priorityLabel.setCardBackgroundColor(color)
    }

    override fun setStatus(status: String) {
        this.status.text = status
    }
}