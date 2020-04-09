package com.example.groceryguruapp.db

import android.provider.BaseColumns

object DbContract {

    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "users"
            val COLUMN_USER_ID = "userid"
            val COLUMN_USER_USERNAME = "username"
            val COLUMN_USER_FIRST  = "userfirst"
            val COLUMN_USER_LAST = "userlast"
            val COLUMN_USER_LISTS = "userlists"
            val COLUMN_USER_EMAIL = "useremail"
            val COLUMN_USER_PASSWORD = "userpassword"
            val COLUMN_USER_ISDEVELOPER = "isdeveloper"
        }
    }

    class GroceryListEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "shoppinglists"
            val COLUMN_LIST_ID = "listid"
            val COLUMN_LIST_NAME = "listname"
            val COLUMN_LIST = "listitems"
        }
    }

    class ItemEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "items"
            val COLUMN_ITEM_ID = "itemid"
            val COLUMN_ITEM_NAME = "itemname"
            val COLUMN_ITEM_CATEGORY = "itemcategory"
        }
    }
}