package com.example.notforgot.ui.fragments

import android.os.Bundle
import android.os.TokenWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.models.network.ApiInteractions
import com.example.notforgot.models.network.Network
import com.example.notforgot.models.network.Task
import com.example.notforgot.models.network.TaskForm
import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_main_screen_with_notes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainScreenWithNotesFragment : Fragment(), MainRecyclerAdapter.ItemListener,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var recyclerAdapter: MainRecyclerAdapter
    private lateinit var recyclerObjectsList: ArrayList<RecyclerObject>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_screen_with_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerObjectsList = ArrayList()
        updateValues()
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

    override fun onNoteStateChanged(item: Task, position: Int) {
        var error: String? = null
        GlobalScope.launch(Dispatchers.Main) {
            val newItem = withContext(Dispatchers.IO) {
                try {
                    ApiInteractions(Network.getInstance(), requireContext()).updateTask(
                        item.taskId, TaskForm(
                            item.title,
                            item.description,
                            if (item.done == 1)
                                0
                            else
                                1,
                            item.deadline,
                            item.category.categoryId,
                            item.priority.id
                        )
                    )
                } catch (e: Exception) {
                    error = "Unable to change state now."
                }

            }
            if (error == null) {
                item.done = (newItem as Task).done
            } else {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
        //item.checkBoxCondition = !item.checkBoxCondition
        //AppDatabase.get(requireActivity()).getItemDao().insertItem(item)
        recyclerAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.app_name)
        updateValues()
    }

    override fun onNoteClicked(item: Task, position: Int) {
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
                        GlobalScope.launch(Dispatchers.Main) {
                            withContext(Dispatchers.IO) {
                                ApiInteractions(Network.getInstance(), requireContext()).deleteTask(
                                    (recyclerObjectsList[position].item as Task).taskId
                                )
                            }
                            /*AppDatabase.get(context!!).getItemDao()
                            .deleteItem(recyclerObjectsList[position].item as Task)*/
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

            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

    override fun onRefresh() {
        updateValues()
    }


    private fun updateValues() {
        GlobalScope.launch(Dispatchers.Main) {
            val categoriesList = withContext(Dispatchers.IO) {
                ApiInteractions(Network.getInstance(), requireContext()).getAllCategories()
            }
            val tasksList = withContext(Dispatchers.IO) {
                ApiInteractions(Network.getInstance(), requireContext()).getAllTasks()
            }
            recyclerObjectsList.clear()
            categoriesList.forEach {
                val tasksOfCategory: List<Task> = tasksList.filter { s -> s.category == it }
                if (!tasksOfCategory.isEmpty()) {
                    recyclerObjectsList.add(RecyclerObject(Constants.TYPE_TITLE, it))
                    tasksOfCategory.forEach {
                        recyclerObjectsList.add(RecyclerObject(Constants.TYPE_NOTE, it))
                    }
                }
            }
            if(recyclerObjectsList.isEmpty()){
                findNavController().popBackStack()
                findNavController().navigate(R.id.emptyMainScreenFragment)
            }
            MainActivity.setRecyclerObjects(recyclerObjectsList)
            recyclerAdapter.updateList(recyclerObjectsList)
            swiperefresh.isRefreshing = false
        }
    }
}