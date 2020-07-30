package com.example.othregensburg.zapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.zxing.integration.android.IntentIntegrator
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_qr_scanner.*

class QRScannerActivity : AppCompatActivity() {

    private val faccounter : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        btn_qr_scanner_scan.setOnClickListener {
            openScanner()
        }
        btn_qr_scanner_emulate.setOnClickListener {
            emulateScanner()
        }

        val fac = Prefs.getInt(SettingsActivity.FACULTY, -1)
        fetchfaccounter(fac)
    }

    private fun openScanner() {
        val scanner = IntentIntegrator(this) //IntentIntegrator class for ease integration with QR and Barcodes
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) //Only QR-Code allowed
        scanner.initiateScan() //will overwrite onActivityResult
    }

    private fun emulateScanner() {
        val dbgString = "{\"barId\":0,\"key1\":\"0027939fe849205356eb3ac47e4441b5b2781621\",\"key2\":\"0027939fe849205356eb3ac47e4441b5b2781621\",\"key3\":\"\",\"key4\":\"\",\"key5\":\"\"}"
        afterScanSuccess(dbgString)
        return
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) { //If result no null, take content
                if (result.contents == null) { //If content is null, scan gets cancelled
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show() //A toast provides simple feedback
                } else {                     // otherwise it gets scanned
                    //Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_SHORT).show(); //toast.lenght is the duration for showing the toast
                    afterScanSuccess(result.contents)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun afterScanSuccess(input : String) {

        val qrmodel = QRWrapper().qrStringToData(input)
        addKeyToDatabase(qrmodel.barId, qrmodel.key1)
        addKeyToDatabase(qrmodel.barId, qrmodel.key2)
        addKeyToDatabase(qrmodel.barId, qrmodel.key3)
        addKeyToDatabase(qrmodel.barId, qrmodel.key4)
        addKeyToDatabase(qrmodel.barId, qrmodel.key5)
    }

    private fun addKeyToDatabase(barID : Int, key : String)
    {
        if(barID < 0) return
        if(key == "") return

        val fac = Prefs.getInt(SettingsActivity.FACULTY, -1)
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null) return

        val myDatabase = RtDatabase()
        myDatabase.setSuccess {
            val myDatabase  = RtDatabase()

            myDatabase.removeKeyFromList(barID, key)
            myDatabase.addKeyToUser(auth.currentUser!!.uid, barID, key, fac, faccounter)

            Toast.makeText(baseContext, "Success Code",
                Toast.LENGTH_SHORT).show()
        }
        myDatabase.setFail {

           Toast.makeText(baseContext, "Fail Code",
                Toast.LENGTH_SHORT).show()
        }
        myDatabase.isKeyInBarKeyList(barID, key,auth.currentUser!!.uid)
    }
}

private fun fetchfaccounter(facid: Int) : Int  {
    val strfacID = facid.toString()

    val ref = FirebaseDatabase.getInstance().getReference("/faculty_st/$strfacID")
    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val post = dataSnapshot.getValue<FacultyModel>()

            val tempcounter = post?.fackeys
            val faccounter = tempcounter!!.toInt() + 1 //Why does this not update faccounter?

        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    })
    return 0
}
