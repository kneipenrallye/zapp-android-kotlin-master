package com.example.othregensburg.zapp

import com.google.gson.Gson

class QRWrapper {

    fun qrStringToData(jsonString : String) : QrModel{
        val gson = Gson()
        return gson.fromJson(jsonString, QrModel::class.java)
    }

    fun generateQrString(qrmodel : QrModel) : String {
        val gson = Gson()
        return gson.toJson(qrmodel)
    }
}

class QrModel(val barId: Int, val key1 : String, var key2 : String, var key3 : String, var key4 : String, var key5 : String)