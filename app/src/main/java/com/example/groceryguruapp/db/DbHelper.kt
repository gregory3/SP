package com.example.groceryguruapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        // increase db version when you add/remove fields + tables
        val DATABASE_VERSION = 2
        val DATABASE_NAME = "FeedReader.db"

        private val SQL_CREATE_ENTRIES =
            "create table " + DbContract.UserEntry.TABLE_NAME + " (" +
                    DbContract.UserEntry.COLUMN_USER_ID + " LONG PRIMARY KEY, " +
                    DbContract.UserEntry.COLUMN_USER_USERNAME + " TEXT, " +
                    DbContract.UserEntry.COLUMN_USER_FIRST + " TEXT, " +
                    DbContract.UserEntry.COLUMN_USER_LAST + " TEXT, " +
                    DbContract.UserEntry.COLUMN_USER_EMAIL + " TEXT, " +
                    DbContract.UserEntry.COLUMN_USER_PASSWORD + " TEXT, " +
                    DbContract.UserEntry.COLUMN_USER_ISDEVELOPER + " INTEGER DEFAULT 0, " +
                    DbContract.UserEntry.COLUMN_USER_LISTS + " BLOB);" +
                    "create table " + DbContract.GroceryListEntry.TABLE_NAME + " (" +
                    DbContract.GroceryListEntry.COLUMN_LIST_ID + " TEXT PRIMARY KEY, " +
                    DbContract.GroceryListEntry.COLUMN_LIST_NAME + " TEXT, " +
                    DbContract.GroceryListEntry.COLUMN_LIST + " BLOB);" +
                    "create table " + DbContract.ItemEntry.TABLE_NAME + " (" +
                    DbContract.ItemEntry.COLUMN_ITEM_ID + " LONG PRIMARY KEY, " +
                    DbContract.ItemEntry.COLUMN_ITEM_NAME + " TEXT, " +
                    DbContract.ItemEntry.COLUMN_ITEM_CATEGORY + "TEXT);"

        private val SQL_DELETE_ENTRIES = "drop table if exists " + DbContract.UserEntry.TABLE_NAME + ";" +
                "drop table if exists " + DbContract.GroceryListEntry.TABLE_NAME + ";" +
                "drop table if exists " + DbContract.ItemEntry.TABLE_NAME + ";"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


    @Throws(SQLiteConstraintException::class)
    fun insertUser(user: DbModels.User): Boolean {
        // gets data repo in write mode
        val db = writableDatabase

        val values = ContentValues()
        values.put(DbContract.UserEntry.COLUMN_USER_USERNAME, user.username)
        values.put(DbContract.UserEntry.COLUMN_USER_FIRST, user.userfirst)
        values.put(DbContract.UserEntry.COLUMN_USER_LAST, user.userlast)
        values.put(DbContract.UserEntry.COLUMN_USER_EMAIL, user.useremail)
        values.put(DbContract.UserEntry.COLUMN_USER_PASSWORD, user.userpassword)
        values.put(DbContract.UserEntry.COLUMN_USER_LISTS, user.userlists)

        // Comment this line out after you create a developer user in your local database
        // values.put(DbContract.UserEntry.COLUMN_USER_ISDEVELOPER, 1);

        val newRowId = db.insert(DbContract.UserEntry.TABLE_NAME, null, values)
        // create update call to update user to have rowId as userId
        val updateData = ContentValues()
        updateData.put(DbContract.UserEntry.COLUMN_USER_ID, newRowId)
//        values.put(DbContract.UserEntry.COLUMN_USER_USERNAME, user.username)
//        values.put(DbContract.UserEntry.COLUMN_USER_FIRST, user.userfirst)
//        values.put(DbContract.UserEntry.COLUMN_USER_LAST, user.userlast)
//        values.put(DbContract.UserEntry.COLUMN_USER_EMAIL, user.useremail)
//        values.put(DbContract.UserEntry.COLUMN_USER_PASSWORD, user.userpassword)
//        values.put(DbContract.UserEntry.COLUMN_USER_LISTS, user.userlists)

        val where = "username=?";
        val whereArgs = Array(1){user.username};

        try {
            db.update(DbContract.UserEntry.TABLE_NAME, updateData, where, whereArgs)
        } catch (e:Exception){
            throw e
        }

        //db.update(DbContract.UserEntry.TABLE_NAME, updateData, DbContract.UserEntry.COLUMN_USER_USERNAME + " = " + "'" + user.username + "'", null)


        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun insertList(userlist: DbModels.GroceryList): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DbContract.GroceryListEntry.COLUMN_LIST_ID, userlist.listid)
        values.put(DbContract.GroceryListEntry.COLUMN_LIST_NAME, userlist.listname)
        values.put(DbContract.GroceryListEntry.COLUMN_LIST, userlist.list)

        val newRowId = db.insert(DbContract.GroceryListEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun insertItem(item: DbModels.Item): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DbContract.ItemEntry.COLUMN_ITEM_ID, item.itemid)
        values.put(DbContract.ItemEntry.COLUMN_ITEM_NAME, item.itemname)
        values.put(DbContract.ItemEntry.COLUMN_ITEM_CATEGORY, item.itemcategory)

        val newRowId = db.insert(DbContract.ItemEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteList(listid: Long): Boolean {
        val db = writableDatabase

        val selection = DbContract.GroceryListEntry.COLUMN_LIST_ID + " LIKE ?"
        val selectionArgs = arrayOf(listid.toString())

        db.delete(DbContract.GroceryListEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteAllUsers(): Boolean {
        val db = writableDatabase

        db.delete(DbContract.UserEntry.TABLE_NAME, null, null)

        return true
    }

    fun showAllUsers(): ArrayList<DbModels.User> {
        val users = ArrayList<DbModels.User>()
        val db = readableDatabase
        var isdeveloper = false;
        lateinit var cursor: Cursor
        try {
            cursor = db.rawQuery("select * from " + DbContract.UserEntry.TABLE_NAME, null)
        } catch(e: SQLiteException){
            throw e
        }

        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast){
                var userid = cursor.getLong(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_ID))
                var first = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_FIRST))
                var last = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_LAST))
                var email = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_EMAIL))
                var password = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_PASSWORD))
                var userlists = cursor.getBlob(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_LISTS))
                var username = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_USERNAME))
                isdeveloper = cursor.getInt(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_ISDEVELOPER)) > 0


                users.add(DbModels.User(userid, username, first, last, email, password, isdeveloper, userlists))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun validateLoginCredentials(email:String, password:String): Boolean {
        val db = readableDatabase

        lateinit var cursor:Cursor
        try {
            cursor = db.rawQuery("select * from " + DbContract.UserEntry.TABLE_NAME + " where useremail = '" + email + "' AND userpassword = '" + password + "'", null)
        } catch(e: SQLiteException) {
            throw e
        }

        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast) {
                var first =
                    cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_FIRST))
                var email =
                    cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_EMAIL))
                var password =
                    cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_PASSWORD))
                cursor.moveToNext()
            }
            return true
        }
        return false
    }

    fun isDeveloper(email:String): Boolean {
        val db = readableDatabase
        var isDeveloper = false

        lateinit var cursor: Cursor

        try {
            cursor = db.rawQuery("select * from " + DbContract.UserEntry.TABLE_NAME + " where useremail = '" + email + "'", null)
        } catch(e: SQLiteException){
            throw e
        }

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast){
                isDeveloper =
                    cursor.getInt(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_ISDEVELOPER)) > 0;
                cursor.moveToNext()
            }
        }
        return isDeveloper;
    }

    fun readUser(userid: Long): ArrayList<DbModels.User> {
        val users = ArrayList<DbModels.User>()
        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DbContract.UserEntry.TABLE_NAME + " where " +
                    DbContract.UserEntry + " = '" + userid + "'", null)
        } catch(e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var first: String
        var last: String
        var email: String
        var password: String
        var userlists: ByteArray
        var isdeveloper: Boolean
        var username: String

        if (cursor!!.moveToFirst()) {
            while(!cursor.isAfterLast) {
                first = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_FIRST))
                last = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_LAST))
                email = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_EMAIL))
                password = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_PASSWORD))
                isdeveloper = cursor.getInt(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_LISTS)) > 0;
                userlists = cursor.getBlob(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_LISTS))
                username = cursor.getString(cursor.getColumnIndex(DbContract.UserEntry.COLUMN_USER_USERNAME))

                users.add(DbModels.User(userid, username, first, last, email, password, isdeveloper, userlists))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun readList(listid: Long): ArrayList<DbModels.GroceryList> {
        val lists = ArrayList<DbModels.GroceryList>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DbContract.GroceryListEntry.TABLE_NAME + " where " +
                    DbContract.GroceryListEntry.COLUMN_LIST_ID + " = '" + listid + "'", null)
        } catch(e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var listname: String
        var userlist: ByteArray

        if (cursor!!.moveToFirst()) {
            while(!cursor.isAfterLast) {
                listname = cursor.getString(cursor.getColumnIndex(DbContract.GroceryListEntry.COLUMN_LIST_NAME))
                userlist = cursor.getBlob(cursor.getColumnIndex(DbContract.GroceryListEntry.COLUMN_LIST))


                lists.add(DbModels.GroceryList(listid, listname, userlist))
                cursor.moveToNext()
            }
        }
        return lists
    }

    fun readAllLists(): ArrayList<DbModels.GroceryList> {
        val lists = ArrayList<DbModels.GroceryList>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DbContract.GroceryListEntry.TABLE_NAME, null)
        } catch(e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var listid: Long
        var listname: String
        var userlist: ByteArray

        if (cursor!!.moveToFirst()) {
            while(!cursor.isAfterLast) {
                listid = cursor.getLong(cursor.getColumnIndex((DbContract.GroceryListEntry.COLUMN_LIST_ID)))
                listname = cursor.getString(cursor.getColumnIndex(DbContract.GroceryListEntry.COLUMN_LIST_NAME))
                userlist = cursor.getBlob(cursor.getColumnIndex(DbContract.GroceryListEntry.COLUMN_LIST))


                lists.add(DbModels.GroceryList(listid, listname, userlist))
                cursor.moveToNext()
            }
        }
        return lists
    }

    fun readItem(itemid: Long): ArrayList<DbModels.Item> {
        val items = ArrayList<DbModels.Item>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DbContract.ItemEntry.TABLE_NAME + " where " +
                    DbContract.ItemEntry.COLUMN_ITEM_ID + " = '" + itemid + "'", null)
        } catch(e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var name: String
        var category: String

        if (cursor!!.moveToFirst()) {
            while(!cursor.isAfterLast) {
                name = cursor.getString(cursor.getColumnIndex(DbContract.ItemEntry.COLUMN_ITEM_NAME))
                category = cursor.getString(cursor.getColumnIndex(DbContract.ItemEntry.COLUMN_ITEM_CATEGORY))


                items.add(DbModels.Item(itemid, name, category))
                cursor.moveToNext()
            }
        }
        return items
    }
}