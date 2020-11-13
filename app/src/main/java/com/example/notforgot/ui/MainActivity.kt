package com.example.notforgot.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.notforgot.*
import com.example.notforgot.models.CategoryWithItems
import com.example.notforgot.models.PreferenceUtils
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.models.User
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.ui.fragments.EmptyMainScreenFragment
import com.example.notforgot.ui.fragments.MainScreenWithNotesFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    MainScreenWithNotesFragment.DataUpdater,
    EmptyMainScreenFragment.DataUpdater {

    companion object {
        private lateinit var user: User
        fun getUser(): User {
            return user
        }

        private lateinit var listCategories: ArrayList<CategoryWithItems>
        private lateinit var recyclerObjectsList: ArrayList<RecyclerObject>
        fun getCategories(): ArrayList<CategoryWithItems> {
            return listCategories
        }

        fun setCategories(list: ArrayList<CategoryWithItems>) {
            listCategories = list
        }

        fun getRecyclerObjects(): ArrayList<RecyclerObject> {
            return recyclerObjectsList
        }

        fun setRecyclerObjects(list: ArrayList<RecyclerObject>) {
            recyclerObjectsList = list
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra("User")) {
            user = intent.getSerializableExtra("User") as User
        }
        getCategories()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var fragmentsCount = 1
        if (recyclerObjectsList.isEmpty())
            fragmentsCount = 0

        if (fragment.childFragmentManager.backStackEntryCount <= fragmentsCount) {
            PreferenceUtils.deleteEmail(this)
            PreferenceUtils.deletePassword(this)
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        getCategories()
    }

    private fun getCategories(): ArrayList<RecyclerObject> {
        listCategories = AppDatabase.get(application).getCategoryDao()
            .getCategoryWithItems(user.userId) as ArrayList<CategoryWithItems>
        recyclerObjectsList = ArrayList<RecyclerObject>()
        listCategories.forEach {
            if (!it.items.isEmpty()) {
                recyclerObjectsList.add(RecyclerObject(
                    Constants.TYPE_TITLE, it.category))
                it.items.forEach {
                    recyclerObjectsList.add((RecyclerObject(
                        Constants.TYPE_NOTE, it)))
                }
            }
        }
        return recyclerObjectsList
    }

    override fun updateValues() {
        getCategories()
    }

}