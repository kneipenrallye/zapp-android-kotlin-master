package com.example.othregensburg.zapp

import com.google.gson.Gson

class QRWrapper {

    fun QrStringToData( jsonString : String) : QrModel{
        val gson = Gson()
        val qrModel = gson.fromJson(jsonString, QrModel::class.java)
        return qrModel
    }

    fun generateQrString(qrmodel : QrModel) : String {
        val gson = Gson()
        val data = qrmodel
        val jsonString = gson.toJson(data)
        return jsonString
    }
}

class QrModel(val barId: Int, val key1 : String, var key2 : String, var key3 : String, var key4 : String, var key5 : String)