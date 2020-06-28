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


        btn_livemap.setOnClickListener {

        }

        btn_barlist.setOnClickListener {

        }

        btn_notification.setOnClickListener {

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

        }

        btn_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply { }
            startActivity(intent);
        }
    }
}
