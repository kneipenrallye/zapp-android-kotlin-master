package com.example.othregensburg.zapp

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_news.*
import okhttp3.*
import java.io.IOException

class NewsActivity : AppCompatActivity() {

    companion object {
        var newsContext: Context? = null
        var TITLE_EXTRA = "TITLE_EXTRA"
        var DESCRIPTION_EXTRA = "DESCRIPTION_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        recyclerView_news.layoutManager = LinearLayoutManager(this)

        fetchJson()

        val msgTitle = intent.getStringExtra(TITLE_EXTRA)
        val msgDescription = intent.getStringExtra(DESCRIPTION_EXTRA)

        if (!msgTitle.isNullOrEmpty() && !msgDescription.isNullOrEmpty()) {
            pushNotificationAlertFunction(msgTitle, msgDescription);
        }
    }

    fun fetchJson() {

        val url =
            "https://firebasestorage.googleapis.com/v0/b/kneipenrallye2.appspot.com/o/news.json?alt=media&token=ae1c0413-b406-4a78-b5ff-928b2be04464"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(baseContext, "Failed to execute request",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //println(body)

                val gson = GsonBuilder().create()

                val homeFeed = gson.fromJson(body, NewsFeed::class.java)

                runOnUiThread {
                    recyclerView_news.adapter = NewsAdapter(homeFeed)
                }
            }
        })
    }

    private fun pushNotificationAlertFunction(title: String, description: String) {
        val alertDialog = AlertDialog.Builder(this).create()

        alertDialog.setTitle(title)
        alertDialog.setMessage(description)
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }
}
