package com.chattylabs.component.interactive.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat

internal abstract class InteractiveNotificationAdapter(context: Context) : InteractiveNotification {

    protected val notificationManager: NotificationManager = context.applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    protected abstract fun create(notificationId: Int): NotificationCompat.Builder

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected abstract fun provideChannel(): NotificationChannel
}