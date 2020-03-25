package com.example.groceryguruapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.groceryguruapp.db.DbHelper
import com.example.groceryguruapp.db.DbModels
import com.google.android.material.textfield.TextInputEditText


class CreateAccount: Fragment() {

    lateinit var dbHelper: DbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DbHelper(context!!);

        view.findViewById<Button>(R.id.btn_login).setOnClickListener {
            // creates user account
            createUser(view);
            view.findViewById<TextInputEditText>(R.id.input_username).setText("");
            view.findViewById<TextInputEditText>(R.id.input_firstname).setText("");
            view.findViewById<TextInputEditText>(R.id.input_lastname).setText("");
            view.findViewById<TextInputEditText>(R.id.input_email).setText("");
            view.findViewById<TextInputEditText>(R.id.input_password).setText("");
            view.findViewById<TextInputEditText>(R.id.input_re_password).setText("");
            var dialog: Dialog = Dialog(context!!, android.R.style.Theme_Black_NoTitleBar);
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.argb(100, 0, 0, 0)))
            dialog.setContentView(R.layout.create_user_dialog);
            dialog.show();
        }
    }

    fun createUser(view: View) {
        var username = view.findViewById<TextInputEditText>(R.id.input_username).text.toString();
        var firstName = view.findViewById<TextInputEditText>(R.id.input_firstname).text.toString();
        var lastName = view.findViewById<TextInputEditText>(R.id.input_lastname).text.toString();
        var email = view.findViewById<TextInputEditText>(R.id.input_email).text.toString();
        var password = view.findViewById<TextInputEditText>(R.id.input_password).text.toString();
        var retryPassword = view.findViewById<TextInputEditText>(R.id.input_re_password).text.toString();

        if (password.trim().equals(retryPassword.trim())) {


            var result = dbHelper.insertUser(
                DbModels.User(
                    0,
                    username,
                    firstName,
                    lastName,
                    email,
                    password,
                    null
                )
            );
        }
    }

}