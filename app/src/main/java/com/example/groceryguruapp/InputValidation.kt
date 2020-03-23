package com.example.groceryguruapp

import android.app.Activity
import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

class InputValidation constructor(val context: Context) {

    /**
     * method to check InputEditText is filled
     *
     * @param textInputEditText
     * @param textInputlayout
     * @param message
     * @return
     */
    fun isInputEditTextFilled(textInputEditText: TextInputEditText, textInputLayout: TextInputLayout, message: String): Boolean {
        var value: String = textInputEditText.text.toString().trim();
        if (value.isEmpty()) {
            textInputLayout.error = message;
            hideKeyboardForm(textInputEditText);
            return false;
        } else {
            textInputLayout.isErrorEnabled = false;
        }
        return true;
    }

    /**
     * method to check InputEditText has valid email
     *
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    fun isInputEditTextEmail(textInputEditText: TextInputEditText, textInputLayout: TextInputLayout, message: String): Boolean {
        var value: String = textInputEditText.text.toString().trim();
        if(value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.error = message;
            hideKeyboardForm(textInputEditText);
            return false;
        } else {
            textInputLayout.isErrorEnabled = false;
        }
        return true;
    }

    /**
     * method to check if InputEditText has valid email
     *
     * @param textINputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    fun isInputEditTextMatches(textInputEditText1: TextInputEditText, textInputEditText2: TextInputEditText, textInputLayout: TextInputLayout, message: String): Boolean {
        var value1: String = textInputEditText1.text.toString().trim();
        var value2: String = textInputEditText2.text.toString().trim();

        if (!value1.contentEquals(value2)) {
            textInputLayout.error = message;
            hideKeyboardForm(textInputEditText2);
            return false;
        } else {
            textInputLayout.isErrorEnabled = false;
        }
        return true;
    }

    /**
     * Method to hide keyboard
     *
     * @param view
     */
    private fun hideKeyboardForm(view: View){
        var imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager;
        imm.hideSoftInputFromWindow(view.windowToken, WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}