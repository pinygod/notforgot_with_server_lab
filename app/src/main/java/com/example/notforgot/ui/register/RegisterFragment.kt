package com.example.notforgot.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R
import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(), RegisterContract.View {

    private lateinit var presenter: RegisterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = RegisterPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this, resources)

        registerButton.setOnClickListener {
            presenter.onRegisterButtonClick(
                emailText.text.toString(),
                nameText.text.toString(),
                passwordText.text.toString(),
                passwordAgainText.text.toString(),
                requireContext()
            )
        }
        loginButton.setOnClickListener {
            presenter.onLoginButtonClick()
        }
    }

    override fun onDestroyView() {
        presenter.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun showEmailError(error: String) {
        email.error = error
    }

    override fun showNameError(error: String) {
        name.error = error
    }

    override fun showPasswordError(error: String) {
        password.error = error
    }

    override fun showRepeatPasswordError(error: String) {
        passwordAgain.error = error
    }

    override fun startMainActivity() {
        val myIntent = Intent(requireContext(), MainActivity::class.java)
        startActivity(myIntent)
        requireActivity().finish()
    }

    override fun setToolbarTitle(title: String) {
        requireActivity().title = title
    }

    override fun finish() {
        findNavController().popBackStack()
    }
}