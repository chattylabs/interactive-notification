package com.chattylabs.android.interactive.notification

import android.app.IntentService
import android.content.Intent
import com.chattylabs.android.interactive.notification.Node.Utils

internal class NotificationService : IntentService("InteractiveNotificationService") {

    override fun onHandleIntent(intent: Intent?) {
        Node.processFlow(this, intent)

        val service = Intent(applicationContext, Utils.getIntentService(intent))
                .putExtra(MESSAGE_ID, Utils.getMessageId(intent))
                .putExtra(MESSAGE_EXTRA, Utils.getMessageExtras(intent))
                .putExtra(ACTION_ID, Utils.getActionId(intent))
                .putExtra(NOTIFICATION_ID, Utils.getNotificationId(intent))

        application.startService(service)
    }
}