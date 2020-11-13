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
import com.example.notforgot.room.AppDatabase
import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import prog.MD5HashFromString

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
                val user = AppDatabase.get(requireContext()).getUserDao()
                    .checkCredentials(emailString, MD5HashFromString.toMD5Hash(passwordString))
                if (user != null) {
                    setPrefs(emailString, MD5HashFromString.toMD5Hash(passwordString))
                    val myIntent = Intent(requireContext(), MainActivity::class.java)
                    myIntent.putExtra("User", user)
                    startActivity(myIntent)
                    requireActivity().finish()
                } else {
                    errorLayout.error = "Email or password is incorrect"
                }
            }
        }
        registerBtn.setOnClickListener {
            //findNavController().popBackStack()
            findNavController().navigate(R.id.action_register)
        }
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