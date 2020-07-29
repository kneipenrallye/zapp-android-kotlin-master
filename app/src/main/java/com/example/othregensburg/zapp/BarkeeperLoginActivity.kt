package com.example.othregensburg.zapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.othregensburg.zapp.AeSimpleSHA1.SHA1
import com.example.othregensburg.zapp.SettingsActivity.Companion.IS_SIGNED_IN_BARKEPPER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_barkeeper_login.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class BarkeeperLoginActivity : AppCompatActivity() {

    private var TAG = "BARKEEPER"
    lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barkeeper_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        check_signed_in()

        btn_barkeeper_login.setOnClickListener {
            login_process()
        }
    }

    private fun login_process() {
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

                    check_signed_in()
                    //saveUserToFirebaseDatabase()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                    Prefs.putBoolean(IS_SIGNED_IN_BARKEPPER, false)
                    //updateUI(null)
                    // ...
                }

                // ...
            }
    }

    private fun check_signed_in() {
        // change activity to logout
        if (auth.currentUser != null && Prefs.getBoolean(IS_SIGNED_IN_BARKEPPER, false)) {
            val intent = Intent(this, QRCodeGenerator::class.java).apply { }
            startActivity(intent)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        TODO("Not yet implemented")
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveUserToFirebaseDatabase() {

        val uid = FirebaseAuth.getInstance().uid
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        val hash = SHA1(formatted)

        val ref = FirebaseDatabase.getInstance().getReference("/barkeeper/$uid")

        val user = DB_Barkeeper("Name", formatted, hash)
        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(
                    baseContext, "Saved.",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun getBarInformation() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/bars/$uid")
    }
}

class DB_Bar(val id: Int, val barname: String)
class DB_Barkeeper(val barkeepername: String, val timeStamp: String, val hash: String)