package com.example.notforgot.ui.register

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.example.notforgot.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterPresenter : RegisterContract.Presenter {

    private var view: RegisterContract.View? = null
    private var model: RegisterContract.Model? = RegisterModel()

    override fun onRegisterButtonClick(
        emailString: String,
        nameString: String,
        passwordString: String,
        passwordRepeatString: String,
        context: Context
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            if (!model!!.validateEmail(emailString)) {
                view!!.showEmailError("E-mail does not match the e-mail pattern")
                return@launch
            }
            if (!model!!.validateName(nameString)){
                view!!.showNameError("It is necessarily")
                return@launch
            }
            if (!model!!.validatePassword(passwordString)){
                view!!.showPasswordError("It is necessarily")
                return@launch
            }
            if (!model!!.validatePassword(passwordRepeatString)){
                view!!.showRepeatPasswordError("It is necessarily")
                return@launch
            }
            if (!model!!.checkPasswordsMatch(passwordString, passwordRepeatString)){
                view!!.showRepeatPasswordError("The fields does not match")
                return@launch
            }


            val token = withContext(Dispatchers.IO) {
                model?.tryToRegister(emailString, passwordString, nameString, context)
            }
            if (token == null) {
                view?.showError("Something went wrong...")
            } else { //successfully logged in
                view?.startMainActivity()
            }
        }
    }

    override fun attachView(view: RegisterContract.View, resources: Resources) {
        this.view = view
        this.view?.setToolbarTitle(resources.getString(R.string.register))
    }

    override fun onDestroyView() {
        this.view = null
    }

    override fun onDestroy() {
        Log.d("RegisterPresenter", "Destroy")
        this.model = null
    }

    override fun onLoginButtonClick() {
        view?.finish()
    }
}