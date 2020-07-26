package com.example.othregensburg.zapp

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_barkeeper_logout.*
import kotlinx.android.synthetic.main.activity_q_r_code_generator.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QRCodeGenerator : AppCompatActivity() {

    private val MINIMUM_KEYS = 10
    private val MAXIMUM_KEYS = 20

    private var bk_barid : Int = -1
    private var bk_barname : String = ""
    private var bk_secret : String = ""
    private var local_key_list = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_code_generator)

        btn_qr_1.setOnClickListener { generateQRCode(1) }
        btn_qr_2.setOnClickListener { generateQRCode(2) }
        btn_qr_3.setOnClickListener { generateQRCode(3) }
        btn_qr_4.setOnClickListener { generateQRCode(4) }
        btn_qr_5.setOnClickListener { generateQRCode(5) }

        // go to main if user is not signed in
        not_signed_in()

        fetchBarID()
    }

    private fun setQrCode(qrString : String)
    {
        try {
            val encoder = BarcodeEncoder()
            val bitmap = encoder.encodeBitmap(qrString, BarcodeFormat.QR_CODE, 500, 500 )
            iv_barcode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateQRCode(count : Int) {

        if(bk_barid == -1 ||bk_barname == "" || bk_secret == "")
        {
            return
        }
        generateKeys()

        var qrmod = QrModel(bk_barid, getAndRemoveKey(), "", "", "", "")
        if(count >= 2) qrmod.key2 = getAndRemoveKey()
        if(count >= 3) qrmod.key3 = getAndRemoveKey()
        if(count >= 4) qrmod.key4 = getAndRemoveKey()
        if(count >= 5) qrmod.key5 = getAndRemoveKey()

        val qrStr = generateQrString(qrmod)
        lbl_qr_output_string.text = qrStr
        setQrCode(qrStr)
    }
    private fun getAndRemoveKey() : String
    {
        if(local_key_list.count() > 1)
        {
            var temp= local_key_list.get(0)
            local_key_list.removeAt(0)
            return temp
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateKeys() {

        if(local_key_list.count() > MINIMUM_KEYS)
            return

        while(local_key_list.count() < MAXIMUM_KEYS)
        {
            val simpleHash = generateSimpleKey(bk_secret)
            addToKeyList(bk_barid, simpleHash)
            local_key_list.add(simpleHash)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateSimpleKey(secret: String) : String {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        val hash = AeSimpleSHA1.SHA1(formatted + secret)

        return hash
    }

    private fun not_signed_in()
    {
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null || Prefs.getBoolean(SettingsActivity.IS_SIGNED_IN_BARKEPPER, false) == false)
        {
            val intent = Intent (this, MainActivity::class.java).apply {  }
            startActivity(intent)
        }
    }

    private fun fetchBarID() : Int {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser ?: return -1
        val userID = user.uid

        val ref = FirebaseDatabase.getInstance().getReference("/barkeeper/$userID")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<barKeeperModel>()
                //val post = dataSnapshot.getValue(barKeeperModel::class.java)
                if(post == null)
                {
                    Toast.makeText(baseContext, "Fail2 getID.",
                        Toast.LENGTH_SHORT).show()
                    return
                }
                val temp_id = post!!.bar_id;
                bk_barid = temp_id!!.toInt()

                fetchBarName(temp_id)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(baseContext, "Fail getID.",
                    Toast.LENGTH_SHORT).show()
            }
        })
        return 0
    }

    private fun fetchBarName(intBarID : Int) {

        if(intBarID < 0 )
            return

        val strBarID = intBarID.toString()

        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys/$strBarID")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<barKeysModel>()
                //val post = dataSnapshot.getValue(barKeeperModel::class.java)
                if(post == null)
                {
                    Toast.makeText(baseContext, "Fail4 getID.",
                        Toast.LENGTH_SHORT).show()
                    return
                }
                val temp_name = post!!.name
                bk_barname = temp_name.toString()
                bk_secret = post!!.secret_code!!
                lbl_qr_barname.text = bk_barname

                Toast.makeText(baseContext, "Success ID: " + temp_name.toString(),
                    Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(baseContext, "Fail3 getID.",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addToKeyList(bar_id: Int, bar_key : String) {

        val strBarID = bar_id.toString()
        if(bar_id < 0)
            return

//        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys/$strBarID/key_liste")
        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys").child(strBarID).child("key_liste").child(bar_key)

        val enable = 1
        ref.setValue(enable)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Saved.",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun generateQrString(qrmodel : QrModel) : String {
        var gson = Gson()
        var data = qrmodel
        var jsonString = gson.toJson(data)
        return jsonString
    }

    private fun QrStringToData( jsonString : String) : QrModel{
        var gson = Gson()
        var qrModel = gson.fromJson(jsonString, QrModel::class.java)
        return qrModel
    }
}

class QrModel(val barId: Int, val key1 : String, var key2 : String, var key3 : String, var key4 : String, var key5 : String)

@IgnoreExtraProperties
data class barKeeperModel(
    var bar_id: Int? = 0
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "bar_id" to bar_id
        )
    }
}

@IgnoreExtraProperties
data class barKeysModel(
    var bar_id: Int? = 0,
    var id : Int? = 0,
    var name : String? = "",
    var secret_code: String? = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to bar_id,
            "name" to name,
            "secret_code" to secret_code
        )
    }
}
