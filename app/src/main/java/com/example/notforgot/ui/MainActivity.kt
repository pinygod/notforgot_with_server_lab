package com.example.notforgot.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.notforgot.*
import com.example.notforgot.models.PreferenceUtils
import com.example.notforgot.models.RecyclerObject
import com.example.notforgot.models.network.*
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.ui.fragments.EmptyMainScreenFragment
import com.example.notforgot.ui.fragments.MainScreenWithNotesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var recyclerObjectsList: ArrayList<RecyclerObject>

        fun setRecyclerObjects(list: ArrayList<RecyclerObject>) {
            recyclerObjectsList = list
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var fragmentsCount = 1
        if (recyclerObjectsList.isEmpty())
            fragmentsCount = 0

        if (fragment.childFragmentManager.backStackEntryCount <= fragmentsCount) {
            PreferenceUtils.deleteUserToken(this)
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }

        return true
    }

}