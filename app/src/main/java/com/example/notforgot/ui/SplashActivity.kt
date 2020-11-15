package com.example.notforgot.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notforgot.models.PreferenceUtils
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.models.network.User


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkLogin()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    private fun checkLogin(): Boolean {
        return PreferenceUtils.getUserToken(this) != null
    }

}