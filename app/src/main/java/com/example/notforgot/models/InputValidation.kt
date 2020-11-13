package com.example.notforgot.models

import android.content.Context
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class InputValidation(context: Context) {
    private val context: Context = context
    fun isInputEditTextFilled(
        textInputEditText: TextInputEditText,
        textInputLayout: TextInputLayout,
        message: String?
    ): Boolean {
        val value = textInputEditText.text.toString().trim { it <= ' ' }
        if (value.isEmpty()) {
            textInputLayout.error = message
            //hideKeyboardFrom(textInputEditText)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    fun isInputEditTextEmail(
        textInputEditText: TextInputEditText,
        textInputLayout: TextInputLayout,
        message: String?
    ): Boolean {
        val value = textInputEditText.text.toString().trim { it <= ' ' }
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.error = message
            //hideKeyboardFrom(textInputEditText)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

    fun isStringsMatches(
        string1: String?,
        string2: String?,
        textInputLayout: TextInputLayout,
        message: String?
    ): Boolean {
        if (!string1.isNullOrEmpty() && !string2.isNullOrEmpty()) {
            val value1 = string1.trim { it <= ' ' }
            val value2 = string2.trim { it <= ' ' }
            if (!value1.contentEquals(value2)) {
                textInputLayout.error = message
                return false
            } else {
                textInputLayout.isErrorEnabled = false
            }
            return true
        } else {
            textInputLayout.error = message
            return false
        }

    }

    fun isInputEditTextMatches(
        textInputEditText1: TextInputEditText,
        textInputEditText2: TextInputEditText,
        textInputLayout: TextInputLayout,
        message: String?
    ): Boolean {
        val value1 = textInputEditText1.text.toString().trim { it <= ' ' }
        val value2 = textInputEditText2.text.toString().trim { it <= ' ' }
        if (!value1.contentEquals(value2)) {
            textInputLayout.error = message
            //hideKeyboardFrom(textInputEditText2)
            return false
        } else {
            textInputLayout.isErrorEnabled = false
        }
        return true
    }

/*    private fun hideKeyboardFrom(view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            view.windowToken,
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }*/

}