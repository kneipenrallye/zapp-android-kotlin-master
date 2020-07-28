package com.example.othregensburg.zapp

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Prefs class
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        // Save context for push notification
        NewsActivity.newsContext = this.applicationContext

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        btn_livemap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java).apply { }
            startActivity(intent)
        }


        btn_barlist.setOnClickListener {
            val intent = Intent(this, BarlistActivity::class.java).apply { }
            startActivity(intent);
        }

        btn_registration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java).apply { }
            startActivity(intent);
        }

        btn_news.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java).apply {
                // In app notification test
                // putExtra(NewsActivity.TITLE_EXTRA, "Push Notification Title")
                // putExtra(NewsActivity.DESCRIPTION_EXTRA, "Push Notification Description")
            }
            startActivity(intent);
        }

        btn_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply { }
            startActivity(intent)
        }

        btn_barkeeper_login.setOnClickListener {
            val intent = Intent(this, BarkeeperLoginActivity::class.java).apply { }
            startActivity(intent)
        }

        btn_scanner.setOnClickListener {
            val intent = Intent(this, QRScannerActivity::class.java).apply { }
            startActivity(intent)
        }
    }
}
