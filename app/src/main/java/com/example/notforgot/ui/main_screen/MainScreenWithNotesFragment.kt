package com.example.notforgot.ui.main_screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.notforgot.R
import com.example.notforgot.adapters.MainRecyclerAdapter
import com.example.notforgot.models.network.data.Task
import com.example.notforgot.ui.SplashActivity
import kotlinx.android.synthetic.main.fragment_main_screen_with_notes.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainScreenWithNotesFragment : Fragment(), MainRecyclerAdapter.ItemListener,
    SwipeRefreshLayout.OnRefreshListener, MainScreenWithNotesContract.View {

    private lateinit var presenter: MainScreenWithNotesContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MainScreenWithNotesPresenter()
        presenter.onCreate(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_screen_with_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this, requireContext(), resources)

        addSwipeGestures()
        swiperefresh.setOnRefreshListener(this)

        exitButton.setOnClickListener {
            presenter.onExitButtonClick(requireContext())
        }
        addNoteButton.setOnClickListener {
            presenter.onAddTaskClick()
        }
    }

    override fun onNoteStateChanged(item: Task, position: Int) {
        presenter.onTaskStateChange(requireContext(), item, position)
    }

    override fun onDestroyView() {
        presenter.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun addSwipeGestures() {
        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    presenter.onSwipeDelete(requireContext(), viewHolder)
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

    override fun onNoteClicked(item: Task, position: Int) {
        presenter.onTaskClick(item, position)
    }

    override fun openTask(task: Task) {
        val action =
            MainScreenWithNotesFragmentDirections.actionMainScreenWithNotesFragmentToNoteDetailsFragment(
                task
            )
        findNavController().navigate(action)
    }

    override fun gotoAddTask() {
        findNavController().navigate(MainScreenWithNotesFragmentDirections.actionMainScreenWithNotesFragmentToCreateNoteFragment())
    }

    override fun onRefresh() {
        presenter.onSwipeRefresh(requireContext())
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

    override fun showEmptyScreen() {
        findNavController().popBackStack()
    }

    override fun stopRefreshAnimation() {
        swiperefresh.isRefreshing = false
    }

    override fun attachRecyclerAdapter(adapter: MainRecyclerAdapter) {
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }


}