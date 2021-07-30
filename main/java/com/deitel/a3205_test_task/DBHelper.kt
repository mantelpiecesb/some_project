package com.deitel.a3205_test_task

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) : SQLiteOpenHelper(context, "my_db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE DOWNLOADS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "REPO_OWNER_NAME TEXT, "
                    + "REPO_FULL_NAME TEXT);"
        )
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}

        private fun insertData(db: SQLiteDatabase, repo_owner_name: String, repo_full_name: String) {
            val values = ContentValues()
            values.put("REPO_OWNER_NAME", repo_owner_name)
            values.put("REPO_FULL_NAME", repo_full_name)
            db.insert("DOWNLOADS", null, values)
        }

}