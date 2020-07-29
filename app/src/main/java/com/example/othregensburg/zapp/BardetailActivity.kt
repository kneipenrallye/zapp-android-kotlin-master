package com.example.othregensburg.zapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_bardetail.*

class BardetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bardetail)

        val navBarTitle = intent.getStringExtra(CustomViewHolder.BAR_TITLE_KEY)
        supportActionBar?.title = navBarTitle

        val locationTextView: TextView = findViewById(R.id.lbl_location_content)
        val locString = intent.getStringExtra(CustomViewHolder.BAR_LOCATION_KEY)
        locationTextView.text = locString

        val specialTextView: TextView = findViewById(R.id.lbl_special_content)
        val specialString = intent.getStringExtra(CustomViewHolder.BAR_SPECIAL_KEY)
        specialTextView.text = specialString

        val barBigImageView: ImageView = findViewById(R.id.img_bar_big)
        val barImageView = intent.getStringExtra(CustomViewHolder.BAR_IMAGE_KEY)
        Picasso.get().load(barImageView).fit().into(barBigImageView)

        val barID = intent.getStringExtra(CustomViewHolder.BAR_ID_KEY)

        setStamp(false)

        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            val myDatabase = RtDatabase()
            myDatabase.setSuccess { setStamp(true) }
            myDatabase.setFail { setStamp(false) }
            myDatabase.checkUserStamp(barID.toInt(), uid)
        }
    }

    private fun setStamp(active : Boolean) {
        if(active)
            img_bar_stamp.setImageResource(R.drawable.approved)

        else
            img_bar_stamp.setImageResource(R.drawable.notapproved)
    }
}
