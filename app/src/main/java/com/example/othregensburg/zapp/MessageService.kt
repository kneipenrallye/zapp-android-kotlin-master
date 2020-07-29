package com.example.othregensburg.zapp

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pixplicity.easyprefs.library.Prefs

class MessageService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {

        // Log.d("TAG", "Refreshed token: $token")
        Prefs.putString(SettingsActivity.FCM_TOKEN, token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val title: String? = remoteMessage.notification!!.title.toString()
        val message: String? = remoteMessage.notification!!.body.toString()

        // check if we have a message to display
        if (title != null && message != null) {

            if (NewsActivity.newsContext != null) {

                val intent = Intent(NewsActivity.newsContext, NewsActivity::class.java).apply {
                    putExtra(NewsActivity.TITLE_EXTRA, title)
                    putExtra(NewsActivity.DESCRIPTION_EXTRA, message)
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                NewsActivity.newsContext?.startActivity(intent)
            }

        }

    }
}