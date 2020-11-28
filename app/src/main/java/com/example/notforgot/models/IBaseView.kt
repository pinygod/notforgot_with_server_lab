package com.example.notforgot.models


interface IBaseView {
    fun setToolbarTitle(title: String)
    fun showError(error: String)
    fun finish()
}