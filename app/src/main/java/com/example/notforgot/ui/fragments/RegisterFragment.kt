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
import com.example.notforgot.models.network.UserRegistrationForm
import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class RegisterFragment : Fragment() {

    private var emailString: String = ""
    private var nameString: String = ""
    private var passwordString: String = ""
    private var emailOK: Boolean = false
    private var nameOK: Boolean = false
    private var passwordOK: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerBtn.setOnClickListener {
            validateInputs()

            if (emailOK && nameOK && passwordOK) {
                processRegistration()
            }
        }
        loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_login)
        }
    }

    private fun processRegistration() {
        GlobalScope.launch(Dispatchers.Main) {
            var token = withContext(Dispatchers.IO) {
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
                    null
                }
            }
            if (token == null) { //user not found so trying to register
                token = withContext(Dispatchers.IO) {
                    try {
                        ApiInteractions(
                            Network.getInstance(),
                            requireContext()
                        ).registerUser(
                            UserRegistrationForm(
                                emailString,
                                nameString,
                                passwordString
                            )
                        ).api_token
                    } catch (e: Exception) {
                        null
                    }

                }
            }
            if (token != null) { //successfully registered
                PreferenceUtils.saveUserToken(requireContext(), token)
                loginUser()
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
        requireActivity().setTitle(R.string.register)
    }

/*    private fun addUser(emailString: String, nameString: String, passwordString: String): User {
        val user = User(
            emailString,
            passwordString,
            nameString
        )
        AppDatabase.get(requireContext()).getUserDao().insertUser(user)
        return AppDatabase.get(requireContext()).getUserDao()
            .checkCredentials(emailString, passwordString)
    }*/

    private fun validateInputs() {
        if (InputValidation(requireContext())
                .isInputEditTextEmail(emailText, email, "It is necessarily")
        ) {
            emailString = emailText.text.toString()
            emailOK = true
        }
        if (InputValidation(requireContext()).isInputEditTextFilled(
                nameText,
                name,
                "It is necessarily"
            )
        ) {
            nameString = nameText.text.toString()
            nameOK = true
        }
        if (InputValidation(requireContext()).isInputEditTextFilled(
                passwordText,
                password,
                "It is necessarily"
            )
        ) {
            if (InputValidation(requireContext()).isInputEditTextMatches(
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
        PreferenceUtils.saveEmail(email, requireContext())
        PreferenceUtils.savePassword(password, requireContext())
    }
}