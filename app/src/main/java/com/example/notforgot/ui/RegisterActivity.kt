package com.example.notforgot.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notforgot.models.PreferenceUtils
import com.example.notforgot.R
import com.example.notforgot.models.InputValidation
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.models.User
import kotlinx.android.synthetic.main.activity_register.*
import prog.MD5HashFromString

class RegisterActivity : AppCompatActivity() {

    private var emailString: String = ""
    private var nameString: String = ""
    private var passwordString: String = ""
    private var emailOK: Boolean = false
    private var nameOK: Boolean = false
    private var passwordOK: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerBtn.setOnClickListener {
            validateInputs()

            if (emailOK && nameOK && passwordOK) {
                passwordString = MD5HashFromString.toMD5Hash(passwordString)
                if (AppDatabase.get(application).getUserDao()
                        .checkCredentials(emailString, passwordString) == null
                ) {
                    val user = addUser(emailString, nameString, passwordString)
                    setPrefs(emailString, passwordString)
                    val myIntent = Intent(this, MainActivity::class.java)
                    myIntent.putExtra("User", user)
                    startActivity(myIntent)
                    finish()
                }
            }
        }
        loginBtn.setOnClickListener {
            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }

    private fun addUser(emailString: String, nameString: String, passwordString: String): User {
        val user = User(
            emailString,
            passwordString,
            nameString
        )
        AppDatabase.get(application).getUserDao().insertUser(user)
        return AppDatabase.get(application).getUserDao()
            .checkCredentials(emailString, passwordString)
    }

    private fun validateInputs() {
        if (InputValidation(this)
                .isInputEditTextEmail(emailText, email, "It is necessarily")
        ) {
            emailString = emailText.text.toString()
            emailOK = true
        }
        if (InputValidation(this).isInputEditTextFilled(
                nameText,
                name,
                "It is necessarily"
            )
        ) {
            nameString = nameText.text.toString()
            nameOK = true
        }
        if (InputValidation(this).isInputEditTextFilled(
                passwordText,
                password,
                "It is necessarily"
            )
        ) {
            if (InputValidation(this).isInputEditTextMatches(
                    passwordText,
                    passwordAgainText,
                    passwordAgain,
                    "The field does not match"
                )
            ) {
                passwordString = passwordText.text.toString()
                passwordOK = true
            }
        }
    }

    private fun setPrefs(email: String, password: String) {
        PreferenceUtils.saveEmail(email, this)
        PreferenceUtils.savePassword(password, this)
    }
}