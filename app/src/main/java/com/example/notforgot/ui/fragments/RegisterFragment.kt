package com.example.notforgot.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R
import com.example.notforgot.models.InputValidation
import com.example.notforgot.models.PreferenceUtils
import com.example.notforgot.models.User
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_register.*
import prog.MD5HashFromString

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerBtn.setOnClickListener {
            validateInputs()

            if (emailOK && nameOK && passwordOK) {
                passwordString = MD5HashFromString.toMD5Hash(passwordString)
                if (AppDatabase.get(requireContext()).getUserDao()
                        .checkCredentials(emailString, passwordString) == null
                ) {
                    val user = addUser(emailString, nameString, passwordString)
                    setPrefs(emailString, passwordString)
                    val myIntent = Intent(requireContext(), MainActivity::class.java)
                    myIntent.putExtra("User", user)
                    startActivity(myIntent)
                    requireActivity().finish()
                }
            }
        }
        loginBtn.setOnClickListener {
            //findNavController().popBackStack()
            findNavController().navigate(R.id.action_login)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.register)
    }

    private fun addUser(emailString: String, nameString: String, passwordString: String): User {
        val user = User(
            emailString,
            passwordString,
            nameString
        )
        AppDatabase.get(requireContext()).getUserDao().insertUser(user)
        return AppDatabase.get(requireContext()).getUserDao()
            .checkCredentials(emailString, passwordString)
    }

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