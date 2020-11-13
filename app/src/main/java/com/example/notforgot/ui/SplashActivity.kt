package com.example.notforgot.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notforgot.models.PreferenceUtils
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.models.User


class SplashActivity : AppCompatActivity() {
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkLogin()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("User", user)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    private fun checkLogin(): Boolean {
        val email: String? = PreferenceUtils.getEmail(this)
        val password: String? =
            PreferenceUtils.getPassword(this)
        return if (email != null && password != null)
            checkCredentials(email, password)
        else
            false
    }

    private fun checkCredentials(email: String, password: String): Boolean {
        user = AppDatabase.get(application).getUserDao().checkCredentials(email, password)
        return user != null
    }
}