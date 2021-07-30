package com.deitel.a3205_test_task

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class SearchUsersFragment : Fragment() {

        private val TAG = "SearchGitsFragment"
        private lateinit var recyclerView: RecyclerView
        private lateinit var layoutManager: RecyclerView.LayoutManager
        private lateinit var dataset: ArrayList<FoundUserItem>
        private lateinit var searchView: SearchView
        private lateinit var network : Network
        private lateinit var ENDPOINT : Uri

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
            network = Network()
            ENDPOINT = network.buildSimpleEndpoint("https://api.github.com/search/users")
            dataset = ArrayList()
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_search_users, container, false)

            recyclerView = rootView.findViewById(R.id.recyclerView_search_results)
            layoutManager = LinearLayoutManager(activity)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = SearchUsersAdapter(dataset, activity)

            searchView = rootView.findViewById(R.id.search_view)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $s")
                    updateItems(s)
                    return true
                }

                override fun onQueryTextChange(s: String): Boolean {
                    return false
                }
            })

            return rootView
        }


        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            super.onCreateOptionsMenu(menu, inflater)
            inflater.inflate(R.menu.fragment_search_gits, menu)
        }


        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.show_downloads -> {
                    val transaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                    val downloadsFragment = DownloadsFragment()
                    transaction?.replace(R.id.fragment_container_view, downloadsFragment)
                    transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    transaction?.addToBackStack(null)
                    transaction?.commit()

                    return true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }




        private fun buildUrl(query: String): String? {
            val uriBuilder: Uri.Builder = ENDPOINT.buildUpon().appendQueryParameter("q", query)
            return uriBuilder.build().toString()
        }

        private fun updateItems(query: String)  = runBlocking {
            launch(Dispatchers.IO) {
                Log.d(TAG, "Coroutine is WORKING!")
                val url = buildUrl(query)
                if (url != null) {
                    dataset = findGitItems(url)
                    setupAdapter()
                }
            }
        }

        private fun findGitItems(url: String): ArrayList<FoundUserItem> {
            val items: ArrayList<FoundUserItem> = ArrayList()
            try {
                val jsonString: String = network.getUrlString(url)
                val jsonBody = JSONObject(jsonString)
                parseItems(items, jsonBody)
            } catch (ioe: IOException) {
                Log.e(TAG, "Failed to fetch items", ioe)
            } catch (je: JSONException) {
                Log.e(TAG, "Failed to parse JSON", je)
            }
            return items
        }



        @Throws(IOException::class, JSONException::class)
        fun parseItems(items: MutableList<FoundUserItem>, jsonBody: JSONObject) {
            val JsonArray = jsonBody.getJSONArray("items")
            for (i in 0 until JsonArray.length()) {
                val JsonObject = JsonArray.getJSONObject(i)
                val full_name_from_json = JsonObject.getString("login")
                val item = FoundUserItem(full_name_from_json)
                items.add(item)
            }
        }


        private fun setupAdapter() {
            activity?.runOnUiThread(Runnable {
                recyclerView.adapter = SearchUsersAdapter(dataset, activity)
            })

        }



    }
