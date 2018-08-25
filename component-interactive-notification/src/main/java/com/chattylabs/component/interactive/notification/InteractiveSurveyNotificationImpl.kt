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
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import java.util.*

internal class InteractiveSurveyNotificationImpl(
        context: Context, receiver: Class<out BroadcastReceiver>,
        notificationManager: NotificationManager, contentTitle: CharSequence,
        expandSubtitle: CharSequence, actions: LinkedHashMap<Int, String>) :
        InteractiveNotificationAdapter(
                context, receiver, notificationManager, contentTitle, expandSubtitle, actions) {

    override fun show(notificationId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(provideChannel())
        }
        notificationManager.notify(notificationId, create(notificationId).build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun provideChannel(): NotificationChannel {
        val importance = NotificationManager.IMPORTANCE_HIGH
        return NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
    }

    override fun create(notificationId: Int): NotificationCompat.Builder {
        fun layout(): Int {
            return when {
                actions.size <= 2 -> R.layout.remote_notification_2_icons_right
                else -> R.layout.remote_notification_icons_at_bottom
            }
        }

        fun applyToContent(contentView: RemoteViews, expandSubtitle:CharSequence? = null) {
            contentView.apply {
                setTextViewText(R.id.interactive_notification_title, contentTitle)
                expandSubtitle?.run {
                    setTextViewText(R.id.interactive_notification_info, this)
                    setViewVisibility(R.id.interactive_notification_info, View.VISIBLE)
                }?:setBoolean(R.id.interactive_notification_title, "setSingleLine", false)
            }
        }

        fun applyChildren(contentView: RemoteViews, iconSize: Float, increment: Int): Int {
            var next = increment
            return actions.toList().forEachIndexed { index, pair ->
                next += index
                val pendingAction = Intent(context, receiver)
                        .putExtra(ACTION_ID, pair.first)
                        .putExtra(NOTIFICATION_ID, notificationId).let {
                            PendingIntent.getBroadcast(
                                    context, next, it, PendingIntent.FLAG_CANCEL_CURRENT)
                        }
                contentView.setTextViewText(pair.first, pair.second)
                contentView.setViewVisibility(pair.first, View.VISIBLE)
                contentView.setTextViewTextSize(pair.first, TypedValue.COMPLEX_UNIT_SP, iconSize)
                contentView.setOnClickPendingIntent(pair.first, pendingAction)
            }.run { next }
        }

        var index = 0

        val contentView = RemoteViews(context.packageName, layout()).apply {
            applyToContent(this, expandSubtitle).also {
                index = applyChildren(this, 25f, index) }
        }

        val bigContentView = RemoteViews(context.packageName, layout()).apply {
            applyToContent(this).also { applyChildren(this, 30f, index) }
        }

        val dismissIntent = Intent(context, receiver).putExtra(NOTIFICATION_ID, notificationId)
        val dismissPendingAction = PendingIntent.getBroadcast(
                context, notificationId, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(context.applicationInfo.icon)
                .setTicker(contentTitle)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(contentView)
                .setCustomBigContentView(bigContentView)
                .setCustomHeadsUpContentView(bigContentView)
                .setDeleteIntent(dismissPendingAction)
                .setLocalOnly(true)
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_MAX)
    }

    companion object {

        private val CHANNEL_ID = "channelId"
        private val CHANNEL_NAME = "Interactive Notification"
    }
}
