package com.example.notforgot.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.notforgot.Constants
import com.example.notforgot.R
import com.example.notforgot.adapters.MainRecyclerAdapter
import com.example.notforgot.models.Note
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_main_screen_with_notes.*

class MainScreenWithNotesFragment : Fragment(), MainRecyclerAdapter.ItemListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var recyclerAdapter: MainRecyclerAdapter
    private lateinit var recyclerObjectsList: ArrayList<RecyclerObject>
    private lateinit var listener: DataUpdater

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DataUpdater
        } catch (castException: ClassCastException) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_screen_with_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerObjectsList = MainActivity.getRecyclerObjects()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = MainRecyclerAdapter(
            recyclerObjectsList,
            requireContext(),
            this
        )
        recyclerView.adapter = recyclerAdapter
        addSwipeGestures()

        swiperefresh.setOnRefreshListener(this)
        addNoteButton.setOnClickListener {
            findNavController().navigate(R.id.createNoteFragment)
        }
    }

    override fun onNoteStateChangedToFalse(item: Note, position: Int) {
        item.checkBoxCondition = !item.checkBoxCondition
        AppDatabase.get(requireActivity()).getItemDao().insertItem(item)
        recyclerAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.app_name)
        listener.updateValues()
        recyclerObjectsList = MainActivity.getRecyclerObjects()
        recyclerAdapter.updateList(recyclerObjectsList)
    }

    override fun onNoteClicked(item: Note, position: Int) {
        findNavController().navigate(R.id.noteDetailsFragment, bundleOf("Note" to item))
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

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val position = viewHolder.adapterPosition
                    if (recyclerObjectsList[position].type == Constants.TYPE_NOTE) {
                        AppDatabase.get(context!!).getItemDao()
                            .deleteItem(recyclerObjectsList[position].item as Note)
                        if (recyclerObjectsList.size > position + 1) {
                            if (recyclerObjectsList[position - 1].type == Constants.TYPE_TITLE && recyclerObjectsList[position + 1].type == Constants.TYPE_TITLE) {
                                recyclerObjectsList.removeAt(viewHolder.adapterPosition)
                                recyclerObjectsList.removeAt(position - 1)
                            } else {
                                recyclerObjectsList.removeAt(viewHolder.adapterPosition)
                            }
                        } else {
                            if (recyclerObjectsList[position - 1].type == Constants.TYPE_TITLE) {
                                recyclerObjectsList.removeAt(viewHolder.adapterPosition)
                                recyclerObjectsList.removeAt(position - 1)
                            } else {
                                recyclerObjectsList.removeAt(viewHolder.adapterPosition)
                            }
                        }
                        MainActivity.setRecyclerObjects(recyclerObjectsList)
                        recyclerAdapter.notifyDataSetChanged()
                        if (recyclerObjectsList.isEmpty())
                            findNavController().popBackStack()
                    }
                }

            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }


    interface DataUpdater {
        fun updateValues()
    }

    override fun onRefresh() {
        listener.updateValues()
        recyclerObjectsList = MainActivity.getRecyclerObjects()
        recyclerAdapter.updateList(recyclerObjectsList)
        swiperefresh.isRefreshing = false
    }
}