package com.example.othregensburg.zapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_q_r_scanner.*

class QRScanner : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_scanner)

        //If Button gets clicked, initiate scanner
        btn_scan.setOnClickListener {
            val scanner = IntentIntegrator(this) //IntentIntegrator class for ease integration with QR and Barcodes
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) //Only QR-Code allowed
            scanner.initiateScan() //will overwrite onActivityResult
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) { //If result no null, take content
                if (result.contents == null) { //If content is null, scan gets cancelled
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show(); //A toast provides simple feedback
                } else {                     // otherwise it gets scanned
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_SHORT).show(); //toast.lenght is the duration for showing the toast
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
