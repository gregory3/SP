package com.example.groceryguruapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.groceryguruapp.db.DbHelper
import com.example.groceryguruapp.InputValidation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Login : Fragment() {
    lateinit var dbHelper: DbHelper

    override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View? {
           // Inflate the layout for this fragment
           return inflater.inflate(R.layout.login, container, false)
       }

       override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           super.onViewCreated(view, savedInstanceState)

           dbHelper = DbHelper(context!!);



           view.findViewById<Button>(R.id.btn_login).setOnClickListener {
               var email = view.findViewById<EditText>(R.id.login_input_email).text.toString()
               var password = view.findViewById<EditText>(R.id.login_input_password).text.toString()

               //goes to home screen
               var authenticated = dbHelper.validateLoginCredentials(email, password);
               if(authenticated){
                   findNavController().navigate(R.id.action_login_to_homePage);
                   view.findViewById<EditText>(R.id.login_input_email).setText("");
                   view.findViewById<EditText>(R.id.login_input_password).setText("");
               } else {
                   view.setBackgroundColor(2)
               }

           }
           view.findViewById<Button>(R.id.link_signup).setOnClickListener {
               //goes to profile
               findNavController().navigate(R.id.action_login_to_createAccount)
           }

           view.findViewById<Button>(R.id.to_display_database_information_btn).setOnClickListener {
               findNavController().navigate(R.id.action_login_to_displayDatabaseInformation)
           }

       }
   }




