package com.example.groceryguruapp.db

object DbModels {

    class User(val userid: Long,
               val userfirst: String,
               val userlast: String,
               val userlists: ByteArray)

    class UserList(val listid: Long,
                   val listname: String,
                   val list: ByteArray)

    class Item(val itemid: Long,
               val itemname: String,
               val itemcategory: String)
}