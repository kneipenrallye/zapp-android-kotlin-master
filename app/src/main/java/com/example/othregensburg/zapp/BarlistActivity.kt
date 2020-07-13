package com.example.othregensburg.zapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_barlist.*
import okhttp3.*
import java.io.IOException

class BarlistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barlist)

        recyclerView_barlist.layoutManager = LinearLayoutManager(this)

        fetchJson()
    }


    private fun fetchJson() {

        //println("Try to fetch JSON")

        val url = "https://xtd.myqnapcloud.com:8443/bars.json"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                val homeFeed = gson.fromJson(body, HomeFeed::class.java)

                runOnUiThread {
                    recyclerView_barlist.adapter = barlistAdapter(homeFeed)
                }
            }
        })
    }
}
