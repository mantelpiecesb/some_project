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
import androidx.recyclerview.widget.RecyclerView


class SearchGitsAdapter(private val dataSet: ArrayList<FoundGitsItem>, private val activity: FragmentActivity?) :
        RecyclerView.Adapter<SearchGitsAdapter.ViewHolder>() {

    private lateinit var downloadManager : DownloadManager


    companion object {
        private val TAG = "GitsAdapter"
        lateinit var onDownloadComplete: BroadcastReceiver
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView
        val downloadButton : Button
        val textView2: TextView

        init {
            textView = v.findViewById(R.id.textView)
            textView2 = v.findViewById(R.id.textView2)
            v.setOnClickListener {
                Log.d(TAG, "Element $adapterPosition clicked.")
                val uri = Uri.parse("https://www.github.com/").buildUpon().appendEncodedPath(textView.text as String?).build()
                val intent : Intent = Intent(Intent.ACTION_VIEW, uri)
                if (activity != null) {
                    activity.startActivity(intent)
                }

            }

            downloadButton = v.findViewById(R.id.downloadButton)
            downloadButton.setOnClickListener {
                downloadManager = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val test_uri = Uri.parse("https://www.github.com/").buildUpon()
                    .appendEncodedPath(textView.text as String?)
                    .appendEncodedPath("/archive/refs/heads/master.zip")
                    .build()
                val request = DownloadManager.Request(test_uri)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                val reference: Long = downloadManager.enqueue(request)

                onDownloadComplete = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                        if (reference === id) {
                            Toast.makeText(
                                activity,
                                "Download completed",
                                Toast.LENGTH_LONG
                            ).show()

                            val databaseHelper: SQLiteOpenHelper = DBHelper(activity)
                            try {
                                val db = databaseHelper.writableDatabase
                                val new_downloads = ContentValues()
                                var repo_owner_name = textView2.text as String?
                                var repo_full_name = textView.text as String?

                                new_downloads.put("REPO_OWNER_NAME", repo_owner_name)
                                new_downloads.put("REPO_FULL_NAME", repo_full_name)
                                db.insert("DOWNLOADS", null, new_downloads)
                                db.close()

                            } catch (e:SQLiteException) {
                                val toast: Toast = Toast.makeText(
                                    activity,
                                    "База данных не загрузилась",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                            }
                        }

                    }
                }

                activity.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.repo_row_item, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")
        viewHolder.textView.text = dataSet[position].full_name
        viewHolder.textView2.text = dataSet[position].owner
    }

    override fun getItemCount() = dataSet.size


}
