package com.example.othregensburg.zapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    companion object {
        private var INVALID_FACULTY = -1
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // go to registration
        userHaveNoAccount()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if(currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        updateUIFail()
                    }
                }
        }
        else {
            updateUI(currentUser)
        }

        btn_delete_account.setOnClickListener {
            deleteAccount()
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        val strUid = currentUser?.uid.toString()
        val facultyList = resources.getStringArray(R.array.faculties_de)
        val fac = Prefs.getInt(SettingsActivity.FACULTY, -1)
        val usr = Prefs.getString(SettingsActivity.USERNAME, "UNKNOWN")

        if (fac != INVALID_FACULTY && usr != "UNKNOWN") {
            txt_faculty_content.text = facultyList[fac].toString()
            txt_username_content.text = usr
            txt_uid_content.text = strUid

            val existUserDB = RtDatabase()
            existUserDB.setSuccess {
                //Toast.makeText(baseContext, "success1", Toast.LENGTH_SHORT).show()
            }

            existUserDB.setFail {
                //Toast.makeText(baseContext, "fail1", Toast.LENGTH_SHORT).show()

                // if not we create a user in the database
                val myDatabase = RtDatabase()
                myDatabase.generateDatabaseUserAccount(strUid, fac, usr)
                myDatabase.setSuccess {
                    Toast.makeText(baseContext, "Authentication success.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            existUserDB.existUserInDatabase(strUid)

        } else {
            updateUIFail()
        }
    }

    private fun updateUIFail() {
        Toast.makeText(baseContext, "Account not exists", Toast.LENGTH_SHORT).show()
    }

    private fun deleteAccount() {
        Prefs.putString(SettingsActivity.USERNAME, "UNKNOWN")
        Prefs.putInt(SettingsActivity.FACULTY, -1)

        val intent = Intent(this, MainActivity::class.java).apply { }
        startActivity(intent)
    }

    private fun userHaveNoAccount() {

        // get
        val usr = Prefs.getString(SettingsActivity.USERNAME, "UNKNOWN")
        val fac = Prefs.getInt(SettingsActivity.FACULTY, -1)

        // User already exists
        if (usr == "UNKNOWN" || fac < 0) {
            // normally it should never be executed
            val intent = Intent(this, MainActivity::class.java).apply { }
            startActivity(intent)
        }
    }
}

