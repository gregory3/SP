package cpackage

import com.example.groceryguruapp.R



import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.groceryguruapp.db.DbHelper
import com.example.groceryguruapp.db.DbModels
import com.google.android.material.textfield.TextInputEditText


class ProfilePage: Fragment() {

    lateinit var dbHelper: DbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DbHelper(context!!);

        view.findViewById<Button>(R.id.submit_update_profile_data).setOnClickListener {
            // creates user account
            createUser(view);
            view.findViewById<EditText>(R.id.input_username).setText("");
            view.findViewById<EditText>(R.id.input_firstname).setText("");
            view.findViewById<EditText>(R.id.input_lastname).setText("");
            view.findViewById<EditText>(R.id.input_email).setText("");
            view.findViewById<EditText>(R.id.input_password).setText("");
            view.findViewById<EditText>(R.id.input_re_password).setText("");

//            var dialog = Dialog(context!!);
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.argb(100, 0, 0, 0)))
//            dialog.setContentView(R.layout.create_user_dialog);
//            dialog.show();
        }
    }

    fun createUser(view: View) {
        var username = view.findViewById<EditText>(R.id.input_username).text.toString();
        var firstName = view.findViewById<EditText>(R.id.input_firstname).text.toString();
        var lastName = view.findViewById<EditText>(R.id.input_lastname).text.toString();
        var email = view.findViewById<EditText>(R.id.input_email).text.toString();
        var password = view.findViewById<EditText>(R.id.input_password).text.toString();
        var retryPassword = view.findViewById<EditText>(R.id.input_re_password).text.toString();

        if (password.trim() == retryPassword.trim()) {
            var result = dbHelper.insertUser(
                DbModels.User(
                    0,
                    username,
                    firstName,
                    lastName,
                    email,
                    password,
                    false,
                    null
                )
            );
        }
    }

}