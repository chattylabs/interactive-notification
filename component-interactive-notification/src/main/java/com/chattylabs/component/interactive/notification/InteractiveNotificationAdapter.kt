package com.chattylabs.component.interactive.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat

internal abstract class InteractiveNotificationAdapter (
        val context: Context, val receiver: Class<out BroadcastReceiver>,
        val notificationManager: NotificationManager, val contentTitle: CharSequence,
        val expandSubtitle: CharSequence, val actions: LinkedHashMap<Int, String>) :
        InteractiveNotification {

    protected abstract fun create(notificationId: Int): NotificationCompat.Builder

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected abstract fun provideChannel(): NotificationChannel
}