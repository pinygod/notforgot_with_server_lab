package com.example.notforgot.ui.main_screen.empty

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R
import com.example.notforgot.ui.SplashActivity
import kotlinx.android.synthetic.main.fragment_empty_main_screen.*
import kotlinx.android.synthetic.main.main_toolbar.*

class EmptyMainScreenFragment : Fragment(), EmptyMainScreenContract.View {

    private lateinit var presenter: EmptyMainScreenContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_empty_main_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = EmptyMainScreenPresenter()
        presenter.attachView(this, requireContext(), resources)

        exitButton.setOnClickListener {
            presenter.onExitButtonClick(requireContext())
        }

        addNoteButton.setOnClickListener {
            presenter.onAddTaskClick()
        }

        swiperefreshEmpty.setOnRefreshListener {
            presenter.onSwipeRefresh(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.checkForTasksExistence(requireContext())
    }

    override fun onDestroyView() {
        presenter.onDestroy()
        super.onDestroyView()
    }

    override fun setToolbarTitle(title: String) {
        this.toolbarTitle.text = title
    }

    override fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun finish() {
        requireActivity().finish()
    }

    override fun logout() {
        startActivity(Intent(requireContext(), SplashActivity::class.java))
        requireActivity().finish()
    }

    override fun showTasks() {
        findNavController().navigate(EmptyMainScreenFragmentDirections.actionEmptyMainScreenFragmentToMainScreenWithNotesFragment())
    }

    override fun gotoAddTask() {
        findNavController().navigate(EmptyMainScreenFragmentDirections.actionEmptyMainScreenFragmentToMainScreenWithNotesFragment())
    }

}