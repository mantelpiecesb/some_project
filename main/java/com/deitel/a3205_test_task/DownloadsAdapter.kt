package com.deitel.a3205_test_task

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class DownloadsAdapter(private val dataSet: Cursor) : RecyclerView.Adapter<DownloadsAdapter.ViewHolder>() {

    private val TAG = "DownloadAdapter"

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView
        val textView2: TextView

        init {
            textView = v.findViewById(R.id.downLoadTextView)
            textView2 = v.findViewById(R.id.downLoadTextView2)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.download_row_item, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")
        if(!dataSet.isClosed) {
            viewHolder.textView.text = "Repo:" + dataSet.getString(2)
            viewHolder.textView2.text = "Owner: " + dataSet.getString(1)
        }
        if(!dataSet.isLast)
            dataSet.moveToNext()
        else
            dataSet.close()
            return
    }

    override fun getItemCount():Int {
        return dataSet.count
    }

}