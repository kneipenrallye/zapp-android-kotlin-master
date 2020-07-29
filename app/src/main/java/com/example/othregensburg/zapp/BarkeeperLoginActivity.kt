package com.example.othregensburg.zapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.othregensburg.zapp.SettingsActivity.Companion.IS_SIGNED_IN_BARKEPPER
import com.google.firebase.auth.FirebaseAuth
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_barkeeper_login.*


class BarkeeperLoginActivity : AppCompatActivity() {

    private val TAG = "BARKEEPER"
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barkeeper_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        checkSignedIn()

        btn_barkeeper_login.setOnClickListener {
            loginProcess()
        }
    }

    private fun loginProcess() {
        val email = txt_username.text.toString()
        val password = txt_password.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                    Prefs.putBoolean(IS_SIGNED_IN_BARKEPPER, true)
                    Prefs.putString(SettingsActivity.USERNAME, "Barkeeper")
                    Prefs.putInt(SettingsActivity.FACULTY, 8)

                    checkSignedIn()
                    //saveUserToFirebaseDatabase()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                    Prefs.putBoolean(IS_SIGNED_IN_BARKEPPER, false)
                }

            }
    }

    private fun checkSignedIn() {
        // change activity to logout
        if (auth.currentUser != null && Prefs.getBoolean(IS_SIGNED_IN_BARKEPPER, false)) {
            val intent = Intent(this, QRCodeGenerator::class.java).apply { }
            startActivity(intent)
        }
    }


    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

}

//class DbBar(val id: Int, val barname: String)
//class DbBarkeeper(val barkeepername: String, val timeStamp: String, val hash: String)