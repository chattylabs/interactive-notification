package com.chattylabs.demo.interactive.notification

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.chattylabs.android.interactive.notification.Node

class DemoNotificationsService : IntentService("DemoNotificationsService") {

    override fun onHandleIntent(intent: Intent?) {

        val notificationId = Node.Utils.getNotificationId(intent)
        val actionId = Node.Utils.getActionId(intent)

        Handler(Looper.getMainLooper()).post {
            if (actionId == null)
                Toast.makeText(this.applicationContext, "Id: $notificationId - Dismissed",
                        Toast.LENGTH_LONG).show()
            else {
                Toast.makeText(this.applicationContext, "Id: $notificationId - Action: $actionId",
                        Toast.LENGTH_LONG).show()
            }
        }
    }
}
