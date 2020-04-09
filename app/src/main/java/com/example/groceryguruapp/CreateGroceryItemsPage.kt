package com.example.groceryguruapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.groceryguruapp.db.DbHelper
import com.example.groceryguruapp.db.DbModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateGroceryItemsPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateGroceryItemsPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_grocery_items_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DbHelper(context!!);

        view.findViewById<Button>(R.id.submit_grocery_item_btn).setOnClickListener {
            val groceryItemName = view.findViewById<EditText>(R.id.enter_grocery_item_name_txt).text.toString()
            val groceryItemCategory = view.findViewById<Spinner>(R.id.enter_grocery_item_category_spinner).selectedItem.toString()

            dbHelper.insertItem(DbModels.Items(0, groceryItemName, groceryItemCategory));
        }

        view.findViewById<Button>(R.id.view_grocery_items_btn).setOnClickListener {
            var items = dbHelper.showAllItems();
            var itemsList = ArrayList<String>(items.size);

            for (i in items) {
                itemsList.add("ID: " + i.itemid + "\nItem Name: " + i.itemname + "\nItem Category: " + i.itemcategory);
            }

            val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, itemsList)
            view.findViewById<ListView>(R.id.grocery_items_view).adapter = adapter
        }

        view.findViewById<Button>(R.id.delete_selected_grocery_items_btn).setOnClickListener {

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateGroceryItemsPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateGroceryItemsPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
