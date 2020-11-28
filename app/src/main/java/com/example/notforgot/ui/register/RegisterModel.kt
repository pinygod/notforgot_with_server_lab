package com.example.notforgot.ui.register

import android.content.Context
import com.example.notforgot.models.network.Api
import com.example.notforgot.models.network.data.UserLoginForm
import com.example.notforgot.models.network.data.UserRegistrationForm
import com.example.notforgot.utils.InputValidation
import com.example.notforgot.utils.PreferenceUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class RegisterModel : RegisterContract.Model {
    override suspend fun tryToRegister(
        emailString: String,
        nameString: String,
        passwordString: String,
        context: Context
    ): String? {
        val email = emailString.trim { it <= ' ' }
        val password = passwordString.trim { it <= ' ' }
        val name = nameString.trim { it <= ' ' }

        var token = withContext(Dispatchers.IO) {
            try { // trying to login with this data
                Api.getInstance(context).loginUser(
                    UserLoginForm(
                        email,
                        password
                    )
                ).api_token
            } catch (e: Exception) {
                null
            }
        }
        if (token == null) { //user not found so trying to register
            token = withContext(Dispatchers.IO) {
                try {
                    Api.getInstance(context).registerUser(
                        UserRegistrationForm(
                            email,
                            name,
                            passwordString
                        )
                    ).api_token
                } catch (e: Exception) {
                    null
                }

            }
        }
        if (token != null) { //successfully logged in
            PreferenceUtils.saveUserToken(context, token)
        }
        return token
    }

    override fun validateEmail(emailString: String): Boolean {
        val email = emailString.trim { it <= ' ' }
        return InputValidation().isInputEditTextEmail(email)
    }

    override fun validateName(nameString: String): Boolean {
        val name = nameString.trim { it <= ' ' }
        return InputValidation().isInputEditTextFilled(name)
    }

    override fun validatePassword(passwordString: String): Boolean {
        val password = passwordString.trim { it <= ' ' }
        return InputValidation().isInputEditTextFilled(password)
    }

    override fun checkPasswordsMatch(passwordString1: String, passwordString2: String): Boolean {
        val pw1 = passwordString1.trim { it <= ' ' }
        val pw2 = passwordString2.trim { it <= ' ' }
        return InputValidation().isInputEditTextMatches(pw1, pw2)
    }
}