package com.example.notforgot.ui.register

import android.content.Context
import com.example.notforgot.models.IBaseModel
import com.example.notforgot.models.IBasePresenter
import com.example.notforgot.models.IBaseView

interface RegisterContract {

    interface View: IBaseView {
        fun showEmailError(error: String)
        fun showNameError(error: String)
        fun showPasswordError(error: String)
        fun showRepeatPasswordError(error: String)
        fun startMainActivity()
    }

    interface Presenter: IBasePresenter {
        fun attachView(view: View, resources: android.content.res.Resources)
        fun onLoginButtonClick()
        fun onRegisterButtonClick(
            emailString: String,
            nameString: String,
            passwordString: String,
            passwordRepeatString: String,
            context: Context
        )
    }

    interface Model: IBaseModel {
        suspend fun tryToRegister(
            emailString: String,
            nameString: String,
            passwordString: String,
            context: Context
        ): String?

        fun validateEmail(emailString: String): Boolean
        fun validateName(nameString: String): Boolean
        fun validatePassword(passwordString: String): Boolean
        fun checkPasswordsMatch(passwordString1: String, passwordString2: String): Boolean
    }
}