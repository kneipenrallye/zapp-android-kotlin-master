package com.example.othregensburg.zapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_settings.*

// THIS ACTIVITY IS ONLY FOR DEBUG PURPOSE

class SettingsActivity : AppCompatActivity() {

    companion object{
        var USERNAME = "USERNAME"
        var PASSWORD = "PASSWORD"
        var PUSH_NOTIFICATION = "PUSH_NOTIFICATION"
        var FACULTY = "FACULTY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btn_load.setOnClickListener {
            cb_push_notification.isChecked = Prefs.getBoolean(PUSH_NOTIFICATION, true)
            txt_username.setText(Prefs.getString(USERNAME, "UNKNOWN"))
            txt_password.setText(Prefs.getString(PASSWORD, "UNKNOWN"))
            txt_faculty.setText(Prefs.getInt(FACULTY, -1).toString())
        }

        btn_save.setOnClickListener {
            Prefs.putBoolean(PUSH_NOTIFICATION, cb_push_notification.isChecked)
            Prefs.putString(USERNAME, txt_username.text.toString())
            Prefs.putString(PASSWORD, txt_password.text.toString())
            Prefs.putInt(FACULTY, txt_faculty.text.toString().toInt())
        }
    }
}
