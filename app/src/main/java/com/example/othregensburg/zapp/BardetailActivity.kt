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
        if (uid != null) {
            val myDatabase = RtDatabase()
            myDatabase.setSuccess { set_stamp(true) }
            myDatabase.setFail { set_stamp(false) }
            myDatabase.checkUserStamp(barID.toInt(), uid)
        }
    }

    fun set_stamp(active: Boolean) {
        if (active)
            img_bar_stamp.imageAlpha = 255
        else
            img_bar_stamp.imageAlpha = 0
    }
}
