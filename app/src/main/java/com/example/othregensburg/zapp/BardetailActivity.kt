package com.example.othregensburg.zapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_bardetail.*
import okio.blackholeSink
import java.lang.Exception

class BardetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bardetail)

        val navBarTitle = intent.getStringExtra(CustomViewHolder.BAR_TITLE_KEY)
        supportActionBar?.title = navBarTitle

        val locationTextView: TextView = findViewById<TextView>(R.id.lbl_location_content)
        val locString = intent.getStringExtra(CustomViewHolder.BAR_LOCATION_KEY)
        locationTextView.text = locString

        val specialTextView: TextView = findViewById<TextView>(R.id.lbl_special_content)
        val specialString = intent.getStringExtra(CustomViewHolder.BAR_SPECIAL_KEY)
        specialTextView.text = specialString

        val barBigImageView: ImageView = findViewById<ImageView>(R.id.img_bar_big)
        val barImageView = intent.getStringExtra(CustomViewHolder.BAR_IMAGE_KEY)
        Picasso.get().load(barImageView).fit().into(barBigImageView)

        val barID = intent.getStringExtra(CustomViewHolder.BAR_ID_KEY)

        set_stamp(false)

        val uid = FirebaseAuth.getInstance().uid
        if(uid != null)
        {
            checkUserStamp(barID.toInt(), uid)
        }
    }

    private fun checkUserStamp(bar_id : Int, user_id: String)
    {
        val strBarID = bar_id.toString()
        if(bar_id < 0)
            return

        if(user_id == "")
            return

        val ref = FirebaseDatabase.getInstance().getReference("/user").child(user_id).child("keys").child(strBarID)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val post = dataSnapshot.getValue<String>()
                    if(post == null || post == "")
                    {
                        Toast.makeText(baseContext, "Null or Empty",
                            Toast.LENGTH_SHORT).show()
                        return
                    }
                    set_stamp(true)

                    Toast.makeText(baseContext, "Key: " + post,
                        Toast.LENGTH_SHORT).show()
                }
                catch (e: Exception) {
                    // handler
                    set_stamp(false)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(baseContext, "Fail8 getID.",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun set_stamp(active : Boolean) {
        if(active)
            img_bar_stamp.setImageResource(R.drawable.approved)
        else
            img_bar_stamp.setImageResource(R.drawable.notapproved)
    }
}
