package com.chattylabs.component.interactive.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.RemoteViews
import java.util.*

internal class InteractiveSurveyNotificationImpl(
        context: Context, receiver: Class<out BroadcastReceiver>,
        notificationManager: NotificationManager, title: CharSequence,
        actions: LinkedHashMap<Int, String>, info: CharSequence?) :
        InteractiveNotificationAdapter(
                context, receiver, notificationManager, title, actions, info) {

    override fun show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(provideChannel())
        }
        notificationManager.notify(NOTIFICATION_ID, create().build())
    }

    override fun dismiss() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun provideChannel(): NotificationChannel {
        val importance = NotificationManager.IMPORTANCE_HIGH
        return NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, importance)
    }

    override fun create(): NotificationCompat.Builder {
        val contentView = RemoteViews(context.packageName, when {
            actions.size <= 2 -> R.layout.remote_notification_2_icons_right
            else -> R.layout.remote_notification_icons_at_bottom
        }).apply {
            setTextViewText(R.id.interactive_notification_title, title)
            setTextViewText(R.id.interactive_notification_info, info)
        }
        applyChildren(contentView)

        return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(context.applicationInfo.icon)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(contentView)
                .setLocalOnly(true)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis())
                .setOnlyAlertOnce(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_MAX)
    }

    private fun applyChildren(contentView: RemoteViews) {
        actions.forEach { key, value ->
            val intent = Intent(context, receiver)
            intent.putExtra(ACTION_ID, key)
            val pendingAction = PendingIntent.getBroadcast(
                    context, REQUEST_CODE + key, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            contentView.setTextViewText(key, value)
            contentView.setViewVisibility(key, View.VISIBLE)
            contentView.setOnClickPendingIntent(key, pendingAction)
        }
    }

    companion object {

        private val REQUEST_CODE = 33
        private val NOTIFICATION_ID = 17
        private val CHANNEL_ID = "channelId"
        private val CHANNEL_NAME = "Interactive Notification"
    }
}
