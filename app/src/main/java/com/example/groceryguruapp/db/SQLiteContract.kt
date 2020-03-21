package com.example.groceryguruapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

class SQLiteContract(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertUser(user: dbmodels.User): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(dbschema.UserEntry.COLUMN_USER_ID, user.userid)
        values.put(dbschema.UserEntry.COLUMN_USER_FIRST, user.userfirst)
        values.put(dbschema.UserEntry.COLUMN_USER_LAST, user.userlast)
        values.put(dbschema.UserEntry.COLUMN_USER_LISTS, user.userlists)

        val newRowId = db.insert(dbschema.UserEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun insertList(userlist: dbmodels.UserList): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(dbschema.ListEntry.COLUMN_LIST_ID, userlist.listid)
        values.put(dbschema.ListEntry.COLUMN_LIST_NAME, userlist.listname)
        values.put(dbschema.ListEntry.COLUMN_LIST, userlist.list)

        val newRowId = db.insert(dbschema.ListEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun insertItem(item: dbmodels.Item): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(dbschema.ItemEntry.COLUMN_ITEM_ID, item.itemid)
        values.put(dbschema.ItemEntry.COLUMN_ITEM_NAME, item.itemname)
        values.put(dbschema.ItemEntry.COLUMN_ITEM_CATEGORY, item.itemcategory)

        val newRowId = db.insert(dbschema.ItemEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteList(listid: String): Boolean {
        val db = writableDatabase

        val selection = dbschema.ListEntry.COLUMN_LIST_ID + " LIKE ?"
        val selectionArgs = arrayOf(listid)

        db.delete(dbschema.UserEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readUser(userid: String): ArrayList<dbmodels.User> {
        val users = ArrayList<dbmodels.User>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + dbschema.UserEntry.TABLE_NAME + " where " +
                    dbschema.UserEntry.COLUMN_USER_ID + " = '" + userid + "'", null)
        } catch(e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var first: String
        var last: String
        var userlists: ByteArray

        if (cursor!!.moveToFirst()) {
            while(!cursor.isAfterLast) {
                first = cursor.getString(cursor.getColumnIndex(dbschema.UserEntry.COLUMN_USER_FIRST))
                last = cursor.getString(cursor.getColumnIndex(dbschema.UserEntry.COLUMN_USER_LAST))
                userlists = cursor.getBlob(cursor.getColumnIndex(dbschema.UserEntry.COLUMN_USER_LISTS))

                users.add(dbmodels.User(userid, first, last, userlists))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun readList(listid: String): ArrayList<dbmodels.UserList> {
        val lists = ArrayList<dbmodels.UserList>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + dbschema.ListEntry.TABLE_NAME + " where " +
                    dbschema.ListEntry.COLUMN_LIST_ID + " = '" + listid + "'", null)
        } catch(e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var listname: String
        var userlist: ByteArray

        if (cursor!!.moveToFirst()) {
            while(!cursor.isAfterLast) {
                listname = cursor.getString(cursor.getColumnIndex(dbschema.ListEntry.COLUMN_LIST_NAME))
                userlist = cursor.getBlob(cursor.getColumnIndex(dbschema.ListEntry.COLUMN_LIST))


                lists.add(dbmodels.UserList(listid, listname, userlist))
                cursor.moveToNext()
            }
        }
        return lists
    }

    fun readAllLists(): ArrayList<dbmodels.UserList> {
        val lists = ArrayList<dbmodels.UserList>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + dbschema.ListEntry.TABLE_NAME, null)
        } catch(e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var listid: String
        var listname: String
        var userlist: ByteArray

        if (cursor!!.moveToFirst()) {
            while(!cursor.isAfterLast) {
                listid = cursor.getString(cursor.getColumnIndex((dbschema.ListEntry.COLUMN_LIST_ID)))
                listname = cursor.getString(cursor.getColumnIndex(dbschema.ListEntry.COLUMN_LIST_NAME))
                userlist = cursor.getBlob(cursor.getColumnIndex(dbschema.ListEntry.COLUMN_LIST))


                lists.add(dbmodels.UserList(listid, listname, userlist))
                cursor.moveToNext()
            }
        }
        return lists
    }

    fun readItem(itemid: String): ArrayList<dbmodels.Item> {
        val items = ArrayList<dbmodels.Item>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + dbschema.ItemEntry.TABLE_NAME + " where " +
                    dbschema.ItemEntry.COLUMN_ITEM_ID + " = '" + itemid + "'", null)
        } catch(e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var name: String
        var category: String

        if (cursor!!.moveToFirst()) {
            while(!cursor.isAfterLast) {
                name = cursor.getString(cursor.getColumnIndex(dbschema.ItemEntry.COLUMN_ITEM_NAME))
                category = cursor.getString(cursor.getColumnIndex(dbschema.ItemEntry.COLUMN_ITEM_CATEGORY))


                items.add(dbmodels.Item(itemid, name, category))
                cursor.moveToNext()
            }
        }
        return items
    }

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "FeedReader.db"

        private val SQL_CREATE_ENTRIES =
            "create table " + dbschema.UserEntry.TABLE_NAME + " (" +
                    dbschema.UserEntry.COLUMN_USER_ID + " TEXT PRIMARY KEY, " +
                    dbschema.UserEntry.COLUMN_USER_FIRST + " TEXT, " +
                    dbschema.UserEntry.COLUMN_USER_LAST + " TEXT, " +
                    dbschema.UserEntry.COLUMN_USER_LISTS + " BLOB);" +
                    "create table " + dbschema.ListEntry.TABLE_NAME + " (" +
                    dbschema.ListEntry.COLUMN_LIST_ID + " TEXT PRIMARY KEY, " +
                    dbschema.ListEntry.COLUMN_LIST_NAME + " TEXT, " +
                    dbschema.ListEntry.COLUMN_LIST + " BLOB);" +
                    "create table " + dbschema.ItemEntry.TABLE_NAME + " (" +
                    dbschema.ItemEntry.COLUMN_ITEM_ID + " TEXT PRIMARY KEY, " +
                    dbschema.ItemEntry.COLUMN_ITEM_NAME + " TEXT, " +
                    dbschema.ItemEntry.COLUMN_ITEM_CATEGORY + "TEXT);"

        private val SQL_DELETE_ENTRIES = "drop table if exists " + dbschema.UserEntry.TABLE_NAME + ";" +
                "drop table if exists " + dbschema.ListEntry.TABLE_NAME + ";" +
                "drop table if exists " + dbschema.ItemEntry.TABLE_NAME + ";"
    }
}