package com.example.notforgot.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.notforgot.Constants
import com.example.notforgot.R
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.models.network.ApiInteractions
import com.example.notforgot.models.network.Network
import com.example.notforgot.models.network.Task
import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_empty_main_screen.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmptyMainScreenFragment : Fragment() {

    private lateinit var recyclerObjectsList: ArrayList<RecyclerObject>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_empty_main_screen, container, false)
    }

    override fun onResume() {
        super.onResume()
        recyclerObjectsList = ArrayList()
        requireActivity().setTitle(R.string.app_name)
        updateValues()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNoteButton.setOnClickListener {
            findNavController().navigate(R.id.createNoteFragment)
        }

        swiperefreshEmpty.setOnRefreshListener {
            updateValues()
        }
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
            MainActivity.setRecyclerObjects(recyclerObjectsList)
            swiperefreshEmpty.isRefreshing = false
            if (!recyclerObjectsList.isEmpty())
                findNavController().navigate(R.id.action_show_notes)
        }
    }

}