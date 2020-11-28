package com.example.notforgot.utils

import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class InputValidation() {
    fun isInputEditTextFilled(
        value: String
    ): Boolean {
        if (value.isEmpty()) {
            return false
        }
        return true
    }

    fun isInputEditTextEmail(
        value: String
    ): Boolean {
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            return false
        }
        return true
    }

    fun isInputEditTextMatches(
        value1: String, value2:String
    ): Boolean {
        if (!value1.contentEquals(value2)) {
            return false
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