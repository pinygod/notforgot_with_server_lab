package com.example.notforgot.ui.login

import android.content.Context
import android.content.res.Resources
import com.example.notforgot.models.IBaseModel
import com.example.notforgot.models.IBasePresenter
import com.example.notforgot.models.IBaseView

interface LoginContract {
    interface View : IBaseView {
        fun startMainActivity()
        fun gotoRegister()
    }

    interface Presenter : IBasePresenter {
        fun attachView(view: View, resources: Resources)
        fun onLoginButtonClick(emailString: String, passwordString: String, context: Context)
        fun onRegisterButtonClick()
    }

    interface Model : IBaseModel {
        suspend fun tryToLogin(
            emailString: String,
            passwordString: String,
            context: Context
        ): String?
    }
}