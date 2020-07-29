package com.example.othregensburg.zapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        val url = "https://firebasestorage.googleapis.com/v0/b/kneipenrallye2.appspot.com/o/bars.json?alt=media&token=03fa68e1-35dc-414a-b112-85fac2a97d4c"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(baseContext, "Failed to execute request",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val homeFeed = gson.fromJson(body, HomeFeed::class.java)

                runOnUiThread {
                    recyclerView_barlist.adapter = BarlistAdapter(homeFeed)
                }
            }
        })
    }
}
