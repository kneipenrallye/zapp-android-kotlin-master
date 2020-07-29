package com.example.othregensburg.zapp

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.lang.Exception

class RtDatabase {

    companion object {
        var INVALID_FACULTY = -1
        var INVALID_BAR_ID = -1
        var ONE_VALID_KEY = 1
        var NONE_VALID_KEY = 0
    }

    private var lambdaSuccess : (() -> Unit)? = null
    private var lambdaFail : (() -> Unit)? = null

    fun setSuccess(lmbd: () -> Unit) {
        lambdaSuccess = lmbd
    }

    fun setFail(lmbd: () -> Unit) {
        lambdaFail = lmbd
    }

    fun generateDatabaseUserAccount(uid : String, faculty : Int, username : String) {

        if(uid == "" || faculty == INVALID_FACULTY || username == "")
            return

        val userDbModel = UserDatabaseModel(faculty,"","",uid,username)
        val ref = FirebaseDatabase.getInstance().getReference("/user").child(uid)

        ref.setValue(userDbModel)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFail() }
    }

    fun existUserInDatabase(uid : String) {

        if(uid == "")
            return

        val ref = FirebaseDatabase.getInstance().getReference("/user").child(uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val post = dataSnapshot.getValue<UserDatabaseModel>()
                    if(post == null) {
                        onFail()
                        return
                    }
                    onSuccess()
                }
                catch (e : Exception) {
                    onFail()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onFail()
            }
        })
    }

    fun removeKeyFromList(bar_id: Int, bar_key : String)
    {
        if(bar_id < INVALID_BAR_ID)
            return

        val strBarID = bar_id.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys").child(strBarID).child("key_liste").child(bar_key)
        val enable = NONE_VALID_KEY

        ref.setValue(enable)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFail() }
    }

    fun addKeyToUser( userID : String, bar_id: Int, bar_key : String) {

        if(bar_id == INVALID_BAR_ID ||bar_key == "" || userID == "")
            return

        val ref = FirebaseDatabase.getInstance().getReference("/user").child(userID).child("keys").child(bar_id.toString())

        ref.setValue(bar_key)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFail() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addToKeyList(bar_id: Int, bar_key : String) {

        if(bar_id == INVALID_BAR_ID || bar_key == "")
            return

        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys").child(bar_id.toString()).child("key_liste").child(bar_key)
        val enable = ONE_VALID_KEY

        ref.setValue(enable)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFail() }
    }

    fun checkUserStamp(bar_id : Int, user_id: String)
    {
        val strBarID = bar_id.toString()
        if(bar_id == INVALID_BAR_ID)
            return

        if(user_id == "")
            return

        val ref = FirebaseDatabase.getInstance().getReference("/user").child(user_id).child("keys").child(strBarID)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val post = dataSnapshot.getValue<String>()
                    if(post == null || post == "") {
                        onFail()
                        return
                    }
                    onSuccess()
                }
                catch (e: Exception) {
                    onFail()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                onFail()
            }
        })
    }

    fun isKeyInBarKeyList(bar_id : Int, bar_key: String, uid : String) {

        if(bar_id == INVALID_BAR_ID || bar_key == "" || uid == "")
            return

        val ref = FirebaseDatabase.getInstance().getReference("/bar_keys").child(bar_id.toString()).child("key_liste").child(bar_key)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<Int>()
                //val post = dataSnapshot.getValue(barKeeperModel::class.java)
                if(post == null)
                {
                    onFail()
                    return
                }

                if(post == ONE_VALID_KEY)
                {
                    onSuccess()
                }
                else
                {
                    onFail()
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                onFail()
            }
        })
    }

    fun onSuccess()
    {
        if(lambdaSuccess != null)
            lambdaSuccess?.let { it() }
    }

    fun onFail()
    {
        if(lambdaFail != null)
            lambdaFail?.let { it() }
    }
}

@IgnoreExtraProperties
data class UserDatabaseModel(
    var fakulty: Int? = 0,
    var group_id : String? = "",
    var keys : String? = "",
    var uid: String? = "",
    var username : String? = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "fakulty" to fakulty,
            "group_id" to group_id,
            "keys" to keys,
            "uid" to uid,
            "username" to username
        )
    }
}

@IgnoreExtraProperties
data class BarKeeperModel(
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
data class BarKeysModel(
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

