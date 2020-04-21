package com.example.groceryguruapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.groceryguruapp.db.DbHelper
import com.example.groceryguruapp.db.DbModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateList.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateList : Fragment() {
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
        return inflater.inflate(R.layout.fragment_create_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DbHelper(context!!);
        lateinit var itemsList: ArrayList<String>
        val groceryList = ArrayList<String>()
        lateinit var groceryListObj: DbModels.GroceryList

        view.findViewById<EditText>(R.id.productSearchTextView).setOnClickListener() {
            val items = dbHelper.searchItems(
                view.findViewById<EditText>(R.id.productSearchTextView).text.toString().trim()
            )
            if (items == {}) {
                Toast.makeText(
                    context!!,
                    "Please type something into search bar.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val itemsList = ArrayList<String>(items.size)
                val itemIds = ArrayList<String>(items.size)
                for (i in items) {
                    itemsList.add("Item Name: " + i.itemname);
                    itemIds.add("" + i.itemid);
                }

                val adapter =
                    ArrayAdapter(context!!, android.R.layout.simple_list_item_1, itemsList)
                view.findViewById<ListView>(R.id.itemSearchResultView).adapter = adapter


                var listView = view.findViewById<ListView>(R.id.itemSearchResultView)
                var adapterView = ArrayAdapter<String>(
                    context!!,
                    android.R.layout.simple_list_item_2,
                    android.R.id.text1,
                    itemsList
                )

                listView.adapter = adapter

                listView.onItemClickListener =
                    AdapterView.OnItemClickListener { adapterView, view, i, l ->
                        Toast.makeText(context!!, itemsList[i] + " selected", Toast.LENGTH_SHORT)
                            .show()
                        groceryList.add(itemIds[i])
                    }
            }

            view.findViewById<Button>(R.id.create_grocery_list_btn).setOnClickListener {
                var ids = ByteArray(groceryList.size)
                for (i in groceryList) {
                    ids[0] = i.toByte()
                }
                groceryListObj = DbModels.GroceryList(0, ids)
                val success = dbHelper.insertGroceryList(groceryListObj);

                if (success) {
                    Toast.makeText(context!!, "List Created", Toast.LENGTH_SHORT).show()
                    // in future implementations, we would invoke the api call to perform grocery item search
                }
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
