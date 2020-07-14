package com.example.othregensburg.zapp

import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        btn_livemap.setOnClickListener {

        }

        btn_barlist.setOnClickListener {
            val intent = Intent(this, BarlistActivity::class.java).apply { }
            startActivity(intent);
        }

        btn_notification.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java).apply {
                putExtra(NewsActivity.TITLE_EXTRA, "Push Notification Title")
                putExtra(NewsActivity.DESCRIPTION_EXTRA, "Push Notification Description")
            }
            startActivity(intent);
        }

        btn_registration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java).apply { }
            startActivity(intent);
        }

        btn_account.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java).apply { }
            startActivity(intent);
        }

        btn_news.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java).apply { }
            startActivity(intent);
        }

        btn_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply { }
            startActivity(intent);
        }

        btn_barkeeper_login.setOnClickListener {
            val intent = Intent(this, BarkeeperLoginActivity::class.java).apply { }
            startActivity(intent);
        }
    }
}
