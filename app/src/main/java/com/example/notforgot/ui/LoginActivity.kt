package com.example.notforgot.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notforgot.models.PreferenceUtils
import com.example.notforgot.R
import com.example.notforgot.models.InputValidation
import com.example.notforgot.room.AppDatabase
import kotlinx.android.synthetic.main.activity_login.*
import prog.MD5HashFromString


class LoginActivity : AppCompatActivity() {


    private var emailString: String = ""
    private var passwordString: String = ""
    private var emailOK: Boolean = false
    private var passwordOK: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

/*        loginBtn.setOnClickListener {
            validateInputs()
            if (emailOK && passwordOK) {
                val user = AppDatabase.get(application).getUserDao()
                    .checkCredentials(emailString, MD5HashFromString.toMD5Hash(passwordString))
                if (user != null) {
                    setPrefs(emailString, MD5HashFromString.toMD5Hash(passwordString))
                    val myIntent = Intent(this, MainActivity::class.java)
                    myIntent.putExtra("User", user)
                    startActivity(myIntent)
                    finish()
                } else {
                    errorLayout.error = "Email or password is incorrect"
                }
            }
        }
        registerBtn.setOnClickListener {
            val myIntent = Intent(this, RegisterActivity::class.java)
            startActivity(myIntent)
            finish()
        }*/
    }

/*    private fun validateInputs() {
        if (InputValidation(this).isInputEditTextFilled(
                emailText,
                email,
                "It is necessarily"
            )
        ) {
            if (InputValidation(this).isInputEditTextEmail(
                    emailText,
                    email,
                    "Does not match the e-mail pattern"
                )
            ) {
                emailString = emailText.text.toString()
                emailOK = true
            }
        }

        if (InputValidation(this).isInputEditTextFilled(
                passwordText,
                password,
                "It is necessarily"
            )
        ) {
            passwordString = passwordText.text.toString()
            passwordOK = true
        }
    }

    private fun setPrefs(email: String, password: String) {
        PreferenceUtils.saveEmail(email, this)
        PreferenceUtils.savePassword(password, this)
    }*/
}