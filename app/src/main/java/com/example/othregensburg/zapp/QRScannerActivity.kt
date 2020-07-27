package com.example.othregensburg.zapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_qr_scanner.*

class QRScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        btn_qr_scanner_scan.setOnClickListener {
            openScanner()
        }

    }

    private fun openScanner() {

        var dbgString = "{\"barId\":0,\"key1\":\"e1900844e698dc0ccdbea80b256c8f2d6d3f3dc7\",\"key2\":\"d66e115e5bc80b87680b26f4d794d2bf6bd0f174\",\"key3\":\"\",\"key4\":\"\",\"key5\":\"\"}"
        afterScanSuccess(dbgString)
        return

        val scanner = IntentIntegrator(this) //IntentIntegrator class for ease integration with QR and Barcodes
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) //Only QR-Code allowed
        scanner.initiateScan() //will overwrite onActivityResult
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) { //If result no null, take content
                if (result.contents == null) { //If content is null, scan gets cancelled
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show(); //A toast provides simple feedback
                } else {                     // otherwise it gets scanned
                    //Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_SHORT).show(); //toast.lenght is the duration for showing the toast
                    afterScanSuccess(result.contents)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    fun afterScanSuccess(input : String) {

        lbl_qr_scanner_scanned.text = input

        var qrmodel = QrStringToData(input)
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

        isKeyFromListValid(barID, key)
        //removeKeyFromList(barID, key)
        //addKeyToUser("UEeNyFzHNsXvOCThujFOLADY4nv1", barID, key)
    }

    private fun QrStringToData( jsonString : String) : QrModel{
        var gson = Gson()
        var qrModel = gson.fromJson(jsonString, QrModel::class.java)
        return qrModel
    }



    private fun removeKeyFromList(bar_id: Int, bar_key : String)
    {
        val strBarID = bar_id.toString()
        if(bar_id < 0)
            return

        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys").child(strBarID).child("key_liste").child(bar_key)

        val enable = 0
        ref.setValue(enable)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Saved.",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun addKeyToUser( userID : String, bar_id: Int, bar_key : String)
    {
        val strBarID = bar_id.toString()
        if(bar_id < 0)
            return

        if(bar_key == "")
            return

        if(userID == "")
            return

//        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys/$strBarID/key_liste")
        val ref = FirebaseDatabase.getInstance().getReference("/user").child(userID).child("keys").child(bar_id.toString())

        //val enable = 1
        ref.setValue(bar_key)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Saved.",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun isKeyFromListValid(bar_id : Int, bar_key: String)
    {
        val strBarID = bar_id.toString()
        if(bar_id < 0)
            return

        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys").child(strBarID).child("key_liste").child(bar_key)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<Int>()
                //val post = dataSnapshot.getValue(barKeeperModel::class.java)
                if(post == null)
                {
                    Toast.makeText(baseContext, "Fail4 getID.",
                        Toast.LENGTH_SHORT).show()
                    return
                }
                val temp_name = post

                Toast.makeText(baseContext, "Key Valid Status: " + temp_name.toString(),
                    Toast.LENGTH_SHORT).show()

                if(temp_name == 1)
                {
                    removeKeyFromList(bar_id, bar_key)
                    addKeyToUser("UEeNyFzHNsXvOCThujFOLADY4nv1", bar_id, bar_key)
                }
                else
                {
                    // Nope
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(baseContext, "Fail8 getID.",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}
