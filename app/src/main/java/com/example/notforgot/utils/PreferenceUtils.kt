package com.example.notforgot.utils

import android.content.Context
import android.preference.PreferenceManager
import com.example.notforgot.utils.Constants


object PreferenceUtils {
    fun saveEmail(email: String?, context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putString(Constants.KEY_EMAIL, email)
        prefsEditor.apply()
        return true
    }

    fun getEmail(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(Constants.KEY_EMAIL, null)
    }

    fun deleteEmail(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().remove(Constants.KEY_EMAIL).apply()
    }

    fun savePassword(password: String?, context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putString(Constants.KEY_PASSWORD, password)
        prefsEditor.apply()
        return true
    }

    fun getPassword(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(Constants.KEY_PASSWORD, null)
    }

    fun deletePassword(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().remove(Constants.KEY_PASSWORD).apply()
    }

    fun saveName(surname: String?, context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putString(Constants.KEY_USER_NAME, surname)
        prefsEditor.apply()
        return true
    }

    fun getName(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(Constants.KEY_USER_NAME, null)
    }

    fun getUserToken(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(Constants.KEY_USER_TOKEN, null)
    }

    fun saveUserToken(context: Context, token: String): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putString(Constants.KEY_USER_TOKEN, token)
        prefsEditor.apply()
        return true
    }

    fun deleteUserToken(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().remove(Constants.KEY_USER_TOKEN).apply()
    }

    fun setSynchronization(value: Boolean, context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putBoolean(Constants.KEY_SYNCRONIZATION, value)
    }

    fun getSynchronization(value: Boolean, context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(Constants.KEY_SYNCRONIZATION, true)
    }
}