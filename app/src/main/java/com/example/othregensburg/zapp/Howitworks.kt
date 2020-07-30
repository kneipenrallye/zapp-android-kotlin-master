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

        val onlineUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/kneipenrallye2.appspot.com/o/RENDER.mp4?alt=media&token=e9a9dd91-5577-4054-a4b5-3947d0374204")

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(onlineUri)
        videoView.requestFocus()
        videoView.start()
    }
}