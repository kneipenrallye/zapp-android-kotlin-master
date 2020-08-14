package com.example.othregensburg.zapp

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_howitworks.*

class Howitworks : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howitworks)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        val onlineUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/kneipenrallye2.appspot.com/o/howitworks.mp4?alt=media&token=cc146bed-ad30-45eb-af09-9b969f6022a8")

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(onlineUri)
        videoView.requestFocus()
        videoView.start()
    }
}