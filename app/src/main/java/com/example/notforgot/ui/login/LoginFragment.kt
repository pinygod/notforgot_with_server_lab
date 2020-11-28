package com.example.notforgot.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.notforgot.R

import com.example.notforgot.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), LoginContract.View {

    private lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LoginPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this, resources)

        loginButton.setOnClickListener {
            presenter.onLoginButtonClick(
                emailText.text.toString(),
                passwordText.text.toString(),
                requireContext()
            )
        }
        registerButton.setOnClickListener {
            presenter.onRegisterButtonClick()
        }
    }

    override fun showError(error: String) {
        errorLayout.error = error
    }

    override fun finish() {
        requireActivity().finish()
    }

    override fun startMainActivity() {
        val myIntent = Intent(requireContext(), MainActivity::class.java)
        startActivity(myIntent)
        requireActivity().finish()
    }

    override fun gotoRegister() {
        findNavController().navigate(R.id.action_register)
    }

    override fun setToolbarTitle(title: String) {
        requireActivity().title = title
    }

    override fun onDestroyView() {
        presenter.onDestroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

}