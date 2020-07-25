package com.example.othregensburg.zapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btn_generate
import kotlinx.android.synthetic.main.activity_q_r_code_generator.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_livemap.setOnClickListener {

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

        btn_scanner.setOnClickListener {

            Intent(this, QRScanner::class.java).apply {
                startActivity(this)
            }

        }

        btn_generate.setOnClickListener {

            Intent(this, QRCodeGenerator::class.java).apply {
                startActivity(this)
            }
        }
    }
}
