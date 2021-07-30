package com.deitel.a3205_test_task

import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class Network {

    fun buildSimpleEndpoint(url : String): Uri {
        return Uri
            .parse(url)
            .buildUpon()
            .build()
    }


    fun getUrlBytes(urlSpec: String): ByteArray? {
        val url = URL(urlSpec)
        val connection = url.openConnection() as HttpURLConnection
        return try {
            val out = ByteArrayOutputStream()
            val `in` = connection.inputStream
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException(connection.responseMessage + ": with " + urlSpec)
            }
            var bytesRead = 0
            val buffer = ByteArray(1024)
            while (`in`.read(buffer).also { bytesRead = it } > 0) {
                out.write(buffer, 0, bytesRead)
            }
            out.close()
            out.toByteArray()
        } finally {
            connection.disconnect()
        }
    }

    fun getUrlString(urlSpec: String): String {
        return String(getUrlBytes(urlSpec)!!)
    }



}