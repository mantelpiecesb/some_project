package com.deitel.a3205_test_task

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DownloadsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var dataset: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataset()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_downloads, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView_downloads)
        layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = DownloadsAdapter(dataset)

        return rootView
    }

    private fun initDataset() {
        val databaseHelper: SQLiteOpenHelper = DBHelper(activity)
        val db = databaseHelper.writableDatabase
        val cursor = db.query(
            "DOWNLOADS",
            arrayOf("_id", "REPO_OWNER_NAME", "REPO_FULL_NAME"),
            null,
            null,
            null,
            null,
            null
        )

        cursor.moveToFirst()
        dataset = cursor


    }


}