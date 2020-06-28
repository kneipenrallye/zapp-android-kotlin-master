package com.example.othregensburg.zapp

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_news.*
import okhttp3.*
import java.io.IOException


class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        recyclerView_news.layoutManager = LinearLayoutManager(this)

        fetchJson()
    }

    fun fetchJson() {

        val url = "https://xtd.myqnapcloud.com:8443/news.json"

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
}
