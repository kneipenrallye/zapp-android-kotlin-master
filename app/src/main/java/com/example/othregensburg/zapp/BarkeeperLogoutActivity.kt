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
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_barkeeper_logout.*


class barkeeperLogoutActivity : AppCompatActivity() {

    private var bk_barid : Int = -1
    private var bk_barname : String = ""
    private var bk_secret : String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barkeeper_logout)

        not_signed_in()

        btn_bk_logout.setOnClickListener {
            logout()
        }

        btn_bk_getID.setOnClickListener {
            fetchBarID()
        }

        btn_bk_save_key.setOnClickListener {
            addToKeyList(0, "jo_ganz_geheim")
        }

        btn_bk_remove_key.setOnClickListener {
            removeKeyFromList(0, "jo_ganz_geheim")
        }

        btn_bk_is_valid_key.setOnClickListener {
            isKeyFromListValid(0, "jo_ganz_geheim")
        }

        btn_bk_save_user_key.setOnClickListener {
            addKeyToUser("UEeNyFzHNsXvOCThujFOLADY4nv1", 0, "noch_geheimer")
        }
    }

    private fun logout()
    {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        Prefs.putBoolean(SettingsActivity.IS_SIGNED_IN_BARKEPPER, false);

        val intent = Intent (this, BarkeeperLoginActivity::class.java).apply {  }
        startActivity(intent)
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

    private fun generateQrString(barID : Int, key_1: String, key_2 : String, key_3 : String) : String {
        var gson = Gson()
        var data = QrModel(2,"abc", "def", "ghi")
        var jsonString = gson.toJson(data)
        return jsonString
    }

    private fun QrStringToData( jsonString : String) : QrModel{
        var gson = Gson()
        var qrModel = gson.fromJson(jsonString, QrModel::class.java)
        return qrModel
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
                lbl_bk_logout_id2.text = temp_id.toString()
//                Toast.makeText(baseContext, "Success ID: " + temp_id.toString(),
//                    Toast.LENGTH_SHORT).show()
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

                lbl_bk_logout_name2.text = temp_name
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
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(baseContext, "Fail8 getID.",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}

class QrModel(val barId: Int, val key1 : String, val key2 : String, val key3 : String)

//class barKeysMode(val barId : Int, val barName : String, val secretCode : String)

//data class barKeeperModel(val bar_id : Int)

@IgnoreExtraProperties
data class barKeeperKeyListModel(
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
