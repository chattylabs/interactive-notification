package com.chattylabs.component.interactive.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context

class InteractiveNotificationBuilder(
        private val context: Context,
        private val receiver: Class<out BroadcastReceiver>,
        private val title: CharSequence,
        private val actions: LinkedHashMap<Int, String>) {

    init {
        //TODO: check if actions.key is a resource id
    }

    private val notificationManager: NotificationManager = context.applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val info: CharSequence? = null

    fun build(): InteractiveNotification = InteractiveSurveyNotificationImpl(
            context, receiver, notificationManager, title, actions, info )
}
