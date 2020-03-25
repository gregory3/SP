package com.example.groceryguruapp.db

object DbModels {

    class User(
        val userid: Long,
        val username: String,
        val userfirst: String,
        val userlast: String,
        val useremail: String,
        val userpassword: String,
        val userlists: ByteArray?)

    class GroceryList(val listid: Long,
                       val listname: String,
                       val list: ByteArray)

    class Item(val itemid: Long,
               val itemname: String,
               val itemcategory: String)
}