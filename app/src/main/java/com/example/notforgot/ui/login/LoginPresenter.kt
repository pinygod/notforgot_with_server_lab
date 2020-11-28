package com.example.notforgot.ui.login

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.example.notforgot.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPresenter : LoginContract.Presenter {

    private var view: LoginContract.View? = null
    private var model: LoginContract.Model? = LoginModel()

    override fun onLoginButtonClick(emailString: String, passwordString: String, context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            val error = withContext(Dispatchers.IO) {
                model?.tryToLogin(emailString, passwordString, context)
            }
            if (error != null) {
                view?.showError(error)
            } else { //successfully logged in
                view?.startMainActivity()
            }
        }
    }

    override fun onRegisterButtonClick() {
        view?.gotoRegister()
    }

    override fun attachView(view: LoginContract.View, resources: Resources) {
        this.view = view
        this.view?.setToolbarTitle(resources.getString(R.string.login))
    }

    override fun onDestroyView() {
        this.view = null
    }

    override fun onDestroy() {
        Log.d("LoginPresenter", "Destroy")
        this.model = null
    }
}