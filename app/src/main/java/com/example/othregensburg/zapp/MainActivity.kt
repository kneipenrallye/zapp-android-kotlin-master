package com.example.othregensburg.zapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_livemap.setOnClickListener {
            val intent = Intent (this, MapsActivity::class.java).apply {  }
            startActivity(intent)
        }


        btn_barlist.setOnClickListener {

        }

        btn_notification.setOnClickListener {

        }

        btn_registration.setOnClickListener {

        }

        btn_account.setOnClickListener {

        }

        btn_news.setOnClickListener {

        }

        btn_settings.setOnClickListener {

        }
    }
}
