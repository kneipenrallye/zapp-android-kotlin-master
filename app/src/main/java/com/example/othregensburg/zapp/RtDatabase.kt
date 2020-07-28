package com.example.othregensburg.zapp

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.lang.Exception

class RtDatabase {

    var lambdaSuccess : (() -> Unit)? = null
    var lambdaFail : (() -> Unit)? = null

    fun setSuccess(lmbd: () -> Unit) {
        lambdaSuccess = lmbd
    }

    fun setFail(lmbd: () -> Unit) {
        lambdaFail = lmbd
    }

    fun generateDatabaseUserAccount(uid : String, faculty : Int, username : String) {

        if(uid == "" || faculty < 0 || username == "")
            return

        val userDbModel = userDatabaseModel(faculty,"","",uid,username)

        val ref = FirebaseDatabase.getInstance().getReference("/user").child(uid)

        //val enable = 1
        ref.setValue(userDbModel)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFail()
            }
    }

    fun existUserInDatabase(uid : String) {

        val ref = FirebaseDatabase.getInstance().getReference("/user").child(uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val post = dataSnapshot.getValue<userDatabaseModel>()
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
data class userDatabaseModel(
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


