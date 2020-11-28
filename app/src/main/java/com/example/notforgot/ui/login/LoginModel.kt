package com.example.notforgot.ui.login

import android.content.Context
import com.example.notforgot.utils.InputValidation
import com.example.notforgot.utils.PreferenceUtils
import com.example.notforgot.models.network.Api
import com.example.notforgot.models.network.data.UserLoginForm
import java.lang.Exception

class LoginModel : LoginContract.Model {

    override suspend fun tryToLogin(
        emailString: String,
        passwordString: String,
        context: Context
    ): String? {
        val email = emailString.trim { it <= ' ' }
        val password = passwordString.trim { it <= ' ' }
        var error: String? = validateInputs(email, password)
        if (error != null) {
            return error
        }
        val token = try { // trying to login with this data
            Api.getInstance(context).loginUser(
                UserLoginForm(
                    email,
                    password
                )
            ).api_token
        } catch (e: Exception) {
            if (e.message!! == "HTTP 404 Not Found") {
                error = "Email or password is incorrect"
                null
            } else {
                error = "Something went wrong..."
                null
            }
        }
        return if (token != null) { //successfully logged in
            PreferenceUtils.saveUserToken(context, token)
            error
        } else {
            error
        }
    }

    private fun validateInputs(email: String, password: String): String? {
        if (!InputValidation().isInputEditTextEmail(email)) {
            return "E-mail does not match the e-mail pattern"
        }

        if (!InputValidation().isInputEditTextFilled(password)) {
            return "Enter the password"
        }
        return null
    }

}