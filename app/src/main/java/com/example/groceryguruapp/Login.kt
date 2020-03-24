package com.example.groceryguruapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.groceryguruapp.db.DbHelper
import com.example.groceryguruapp.InputValidation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Login : Fragment() {
    private var activity: Fragment? = null
    private var inputValidation: InputValidation? = null

    private var textInputLayoutEmail: TextInputLayout? = null
    private var textInputLayoutPassword: TextInputLayout? = null

    private var textInputEditTextEmail: TextInputEditText? = null
    private var textInputEditTextPassword: TextInputEditText? = null
    private var sqliteContract: DbHelper? = null


    override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View? {
           // Inflate the layout for this fragment
           return inflater.inflate(R.layout.login, container, false)
       }

       override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           super.onViewCreated(view, savedInstanceState)

           initViews(view);
           //initListeners();
           initObjects();

           view.findViewById<Button>(R.id.btn_login).setOnClickListener {
               //goes to home screen
               findNavController().navigate(R.id.action_login_to_homePage);

           }
           view.findViewById<Button>(R.id.link_signup).setOnClickListener {
               //goes to profile
               findNavController().navigate(R.id.action_login_to_createAccount)
           }

       }

       private fun initViews(view: View) {
           textInputLayoutEmail = view.findViewById<TextInputLayout>(R.id.input_email)
       }

       private fun initObjects() {
           sqliteContract = context?.let { DbHelper(it) };
           inputValidation = context?.let { InputValidation(it) };
       }

       private fun verifyFromSQLite() {
           if(!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayout, message)) {

           }

       }
   }




