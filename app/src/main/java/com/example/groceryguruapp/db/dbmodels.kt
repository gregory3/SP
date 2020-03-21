package com.example.groceryguruapp.db

object dbmodels {

    class User(val userid: String,
               val userfirst: String,
               val userlast: String,
               val userlists: ByteArray)

    class UserList(val listid: String,
                   val listname: String,
                   val list: ByteArray)

    class Item(val itemid: String,
               val itemname: String,
               val itemcategory: String)
}