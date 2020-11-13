package com.example.notforgot.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R
import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_main_screen_with_notes.*

class EmptyMainScreenFragment : Fragment() {

    private lateinit var listener: MainScreenWithNotesFragment.DataUpdater

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as MainScreenWithNotesFragment.DataUpdater
        } catch (castException: ClassCastException) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_empty_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!MainActivity.getRecyclerObjects().isEmpty())
            findNavController().navigate(R.id.action_show_notes)
        addNoteButton.setOnClickListener {
            findNavController().navigate(R.id.createNoteFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.app_name)
        listener.updateValues()
        if (!MainActivity.getRecyclerObjects().isEmpty())
            findNavController().navigate(R.id.action_show_notes)
    }


    interface DataUpdater{
        fun updateValues()
    }

}