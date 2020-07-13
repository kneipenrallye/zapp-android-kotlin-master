package com.example.othregensburg.zapp

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pixplicity.easyprefs.library.Prefs

class MessageService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {

        // Log.d("TAG", "Refreshed token: $token")

        Prefs.putString(SettingsActivity.FCM_TOKEN, token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        //sendRegistrationToServer(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        /*
        Log.d("TAG", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("TAG", "Message data payload: ${remoteMessage.data}")

        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("TAG", "Message Notification Body: ${it.body}")
        }
        */

        val title: String? = remoteMessage.notification!!.title.toString()
        val message: String? = remoteMessage.notification!!.body.toString()

        // check if we have a message to display
        if (title != null && message != null) {

            if(NewsActivity.newsContext != null) {

                val intent = Intent(NewsActivity.newsContext, NewsActivity::class.java).apply {
                    putExtra(NewsActivity.TITLE_EXTRA, title);
                    putExtra(NewsActivity.DESCRIPTION_EXTRA, message);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                NewsActivity.newsContext?.startActivity(intent)
            }

        }

    };
}