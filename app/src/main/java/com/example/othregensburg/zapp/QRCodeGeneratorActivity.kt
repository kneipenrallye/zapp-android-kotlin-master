package com.example.othregensburg.zapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_qr_code_generator.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QRCodeGenerator : AppCompatActivity() {

    companion object {
        var MINIMUM_KEYS = 10
        var MAXIMUM_KEYS = 20
        var INVALID_BAR_ID = -1
        var INVALID_FACULTY = -1
    }

    private var bkBarid: Int = INVALID_BAR_ID
    private var bkBarname: String = ""
    private var bkSecret: String = ""
    private var localKeyList = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_generator)

        btn_qr_1.setOnClickListener { generateQRCode(1) }
        btn_qr_2.setOnClickListener { generateQRCode(2) }
        btn_qr_3.setOnClickListener { generateQRCode(3) }
        btn_qr_4.setOnClickListener { generateQRCode(4) }
        btn_qr_5.setOnClickListener { generateQRCode(5) }

        btn_qr_logout.setOnClickListener {
            logout()
        }

        // go to main if user is not signed in
        notSignedIn()

        fetchBarID()
    }

    private fun setQrCode(qrString: String) {
        try {
            val encoder = BarcodeEncoder()
            val bitmap = encoder.encodeBitmap(qrString, BarcodeFormat.QR_CODE, 500, 500)
            iv_barcode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateQRCode(count: Int) {

        if (bkBarid == INVALID_BAR_ID || bkBarname == "" || bkSecret == "") {
            return
        }
        generateKeys()

        val qrmodel = QrModel(bkBarid, getAndRemoveKey(), "", "", "", "")
        if (count >= 2) qrmodel.key2 = getAndRemoveKey()
        if (count >= 3) qrmodel.key3 = getAndRemoveKey()
        if (count >= 4) qrmodel.key4 = getAndRemoveKey()
        if (count >= 5) qrmodel.key5 = getAndRemoveKey()

        val qrStr = QRWrapper().generateQrString(qrmodel)
        lbl_qr_output_string.text = qrStr
        setQrCode(qrStr)
    }

    private fun getAndRemoveKey(): String {
        if (localKeyList.count() > 1) {
            val temp = localKeyList[0]
            localKeyList.removeAt(0)
            return temp
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateKeys() {

        if (localKeyList.count() > MINIMUM_KEYS)
            return

        while (localKeyList.count() < MAXIMUM_KEYS) {
            val simpleHash = generateSimpleKey(bkSecret)

            val myDatabase = RtDatabase()
            myDatabase.addToKeyList(bkBarid, simpleHash)

            localKeyList.add(simpleHash)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateSimpleKey(secret: String): String {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)

        return AeSimpleSHA1.SHA1(formatted + secret)
    }

    private fun notSignedIn() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null || !Prefs.getBoolean(SettingsActivity.IS_SIGNED_IN_BARKEPPER,false)) {
            val intent = Intent(this, MainActivity::class.java).apply { }
            startActivity(intent)
        }
    }

    private fun logout() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        Prefs.putBoolean(SettingsActivity.IS_SIGNED_IN_BARKEPPER, false)
        Prefs.putString(SettingsActivity.USERNAME, "UNKNOWN")
        Prefs.putInt(SettingsActivity.FACULTY, INVALID_FACULTY)

        val intent = Intent(this, MainActivity::class.java).apply { }
        startActivity(intent)
    }

    private fun fetchBarID(): Int {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser ?: return INVALID_BAR_ID
        val userID = user.uid

        val ref = FirebaseDatabase.getInstance().getReference("/barkeeper/$userID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<BarKeeperModel>()
                //val post = dataSnapshot.getValue(barKeeperModel::class.java)
                if (post == null) {
                    Toast.makeText(
                        baseContext, "Fail fetchBarID",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val tempId = post.bar_id
                bkBarid = tempId!!.toInt()

                fetchBarName(tempId)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    baseContext, "Fail fetchBarID",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return 0
    }

    private fun fetchBarName(intBarID: Int) {

        if (intBarID < 0)
            return

        val strBarID = intBarID.toString()

        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys/$strBarID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<BarKeysModel>()
                //val post = dataSnapshot.getValue(barKeeperModel::class.java)
                if (post == null) {
                    Toast.makeText(
                        baseContext, "fetchBarName",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val tempName = post.name
                bkBarname = tempName.toString()
                bkSecret = post.secret_code!!
                lbl_qr_barname.text = bkBarname

                Toast.makeText(
                    baseContext, "Success ID: " + tempName.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    baseContext, "fetchBarName",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}

