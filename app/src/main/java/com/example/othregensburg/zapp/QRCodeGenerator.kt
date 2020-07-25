package com.example.othregensburg.zapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRCodeGenerator : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_code_generator)

        // Get Id´s to work with
        val etText = findViewById<EditText>(R.id.et_text)
        val btnGen = findViewById<Button>(R.id.btn_generate1)
        val ivBarcode = findViewById<ImageView>(R.id.iv_barcode)

        // Button clicked -> generates QR Code from input TextView
        btnGen.setOnClickListener {
            try {
                val encoder = BarcodeEncoder()
                val bitmap = encoder.encodeBitmap(etText.text.toString(), BarcodeFormat.QR_CODE, 500, 500 ) //First Parameter: what´s hidden in QR Code
                ivBarcode.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
