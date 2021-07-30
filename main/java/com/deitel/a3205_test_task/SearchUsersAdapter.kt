/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.deitel.a3205_test_task

import android.app.DownloadManager
import android.content.*
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView


class SearchUsersAdapter(private val dataSet: ArrayList<FoundUserItem>, private val activity: FragmentActivity?) :
        RecyclerView.Adapter<SearchUsersAdapter.ViewHolder>() {

        private val TAG = "CustomAdapter"

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView

        init {
            textView = v.findViewById(R.id.textView)
            v.setOnClickListener {
                val transaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                val reposFragment = SearchGitsFragment()
                reposFragment.updateItems(textView.text as String)
                transaction?.replace(R.id.fragment_container_view, reposFragment)
                transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.users_row_item, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")
        viewHolder.textView.text = dataSet[position].username
    }

    override fun getItemCount() = dataSet.size


}
