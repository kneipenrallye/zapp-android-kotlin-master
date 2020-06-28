package com.example.othregensburg.zapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

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
    }
}
