package com.chattylabs.component.interactive.notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent

interface InteractiveNotification {

    class Utils {
        companion object {
            @JvmStatic fun getActionId(intent: Intent): Int = intent.extras?.getInt(ACTION_ID) ?:0
            @JvmStatic fun getNotificationId(intent: Intent): Int = intent.extras?.getInt(NOTIFICATION_ID) ?:0
            @JvmStatic fun isDismissed(intent: Intent): Boolean = intent.extras?.getBoolean(NOTIFICATION_DISMISSED) ?:false
        }
    }

    fun show(notificationId: Int)

    companion object {
        fun dismiss(context: Context, notificationId: Int) {
            val notificationManager: NotificationManager = context.applicationContext
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }
}

internal const val ACTION_ID = "action_id"
internal const val NOTIFICATION_ID = "notification_id"
internal const val NOTIFICATION_DISMISSED = "notification_dismissed"