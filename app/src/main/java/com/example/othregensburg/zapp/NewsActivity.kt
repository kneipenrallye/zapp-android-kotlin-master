package com.example.othregensburg.zapp

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_news.*
import okhttp3.*
import java.io.IOException


class NewsActivity : AppCompatActivity() {

    companion object {
        var newsContext : Context? = null
        var TITLE_EXTRA         = "TITLE_EXTRA"
        var DESCRIPTION_EXTRA   = "DESCRIPTION_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        recyclerView_news.layoutManager = LinearLayoutManager(this)

        fetchJson()

        val msgTitle        = intent.getStringExtra(TITLE_EXTRA)
        val msgDescription  = intent.getStringExtra(DESCRIPTION_EXTRA)

        if(!msgTitle.isNullOrEmpty() && !msgDescription.isNullOrEmpty()) {
            pushNotificationAlertFunction(msgTitle, msgDescription);
        }
    }

    fun fetchJson() {

        //val url = "https://xtd.myqnapcloud.com:8443/news.json"
        val url = "https://firebasestorage.googleapis.com/v0/b/kneipenrallye2.appspot.com/o/news.json?alt=media&token=b788c034-4949-4b81-9859-ff61804c7d5e"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
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

    private fun pushNotificationAlertFunction(title : String, description : String)
    {
        val alertDialog = AlertDialog.Builder(this).create()

        alertDialog.setTitle(title)
        alertDialog.setMessage(description)
        alertDialog.setButton( AlertDialog.BUTTON_NEUTRAL, "OK") {
                dialog, which -> dialog.dismiss()
        }
        alertDialog.show()
    }
}