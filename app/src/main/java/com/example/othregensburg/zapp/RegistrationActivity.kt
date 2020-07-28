package com.example.othregensburg.zapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        val spinner: Spinner = findViewById(R.id.spin_faculties)
        ArrayAdapter.createFromResource(
            this,
            R.array.faculties_de,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // if user have account change to account
        userHaveAccount()

        // Select default Faculty
        spinner.setSelection(5)

        btn_register_register.setOnClickListener {

            // get input attribute
            val posFaculties = spin_faculties.selectedItemPosition
            val username = txt_username.text.toString()

            // check input a username
            if (username.isEmpty() && username.length > 3) {
                Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // save attribute
            Prefs.putString(SettingsActivity.USERNAME, username)
            Prefs.putInt(SettingsActivity.FACULTY, posFaculties)

            // change activity
            val intent = Intent(this, AccountActivity::class.java).apply { }
            startActivity(intent);

        }
    }

    private fun userHaveAccount() {

        // get
        val usr = Prefs.getString(SettingsActivity.USERNAME, "UNKNOWN")
        val fac = Prefs.getInt(SettingsActivity.FACULTY, -1)

        // User already exists
        if (usr != "UNKNOWN" && fac >= 0) {
            val intent = Intent(this, AccountActivity::class.java).apply { }
            startActivity(intent);
        }
    }
}
