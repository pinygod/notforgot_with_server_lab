package com.example.notforgot.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R
import com.example.notforgot.models.*
import com.example.notforgot.models.network.ApiInteractions
import com.example.notforgot.models.network.Network
import com.example.notforgot.models.network.UserLoginForm
import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginFragment : Fragment() {

    private var emailString: String = ""
    private var passwordString: String = ""
    private var emailOK: Boolean = false
    private var passwordOK: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBtn.setOnClickListener {
            validateInputs()
            if (emailOK && passwordOK) {
                processLogin()
            }
        }
        registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_register)
        }
    }

    private fun setError(error: String) {
        errorLayout.error = error
    }


    private fun processLogin() {
        GlobalScope.launch(Dispatchers.Main) {
            var error: String = ""
            val token = withContext(Dispatchers.IO) {
                try { // trying to login with this data
                    ApiInteractions(
                        Network.getInstance(),
                        requireContext()
                    ).loginUser(
                        UserLoginForm(
                            emailString,
                            passwordString
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
            }
            if (token != null) { //successfully logged in
                PreferenceUtils.saveUserToken(requireContext(), token)
                loginUser()
            }
            else{
                errorLayout.error = error
            }
        }
    }

    private fun loginUser() {
        val myIntent = Intent(requireContext(), MainActivity::class.java)
        //myIntent.putExtra("User", user)
        startActivity(myIntent)
        requireActivity().finish()
    }


    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.login)
    }

    private fun validateInputs() {
        if (InputValidation(requireContext()).isInputEditTextFilled(
                emailText,
                email,
                "It is necessarily"
            )
        ) {
            if (InputValidation(requireContext()).isInputEditTextEmail(
                    emailText,
                    email,
                    "Does not match the e-mail pattern"
                )
            ) {
                emailString = emailText.text.toString()
                emailOK = true
            }
        }

        if (InputValidation(requireContext()).isInputEditTextFilled(
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
        PreferenceUtils.saveEmail(email, requireContext())
        PreferenceUtils.savePassword(password, requireContext())
    }

}