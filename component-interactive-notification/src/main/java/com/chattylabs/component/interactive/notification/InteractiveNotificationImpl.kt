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
import com.chattylabs.component.interactive.notification.InteractiveNotification.Action

internal class InteractiveNotificationImpl(
        private val context: Context,
        private val receiver: Class<out BroadcastReceiver>,
        private val contentTitle: CharSequence,
        private val expandSubtitle: CharSequence?,
        private val actions: List<Action>) :
        InteractiveNotificationAdapter(context) {

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
        var index = 0

        val contentView = RemoteViews(context.packageName, layout()).also {
            applyToContent(it, expandSubtitle)
            if (actions.size in 1..2) index = applyChildren(it, notificationId, false, index)
        }

        val bigContentView = RemoteViews(context.packageName, layout()).also {
            applyToContent(it)
            applyChildren(it, notificationId, true, index)
        }

        val dismissPendingAction = Intent(context, receiver)
                .putExtra(NOTIFICATION_ID, notificationId)
                .putExtra(NOTIFICATION_DISMISSED, true).let {
                    PendingIntent.getBroadcast(
                            context, notificationId, it, PendingIntent.FLAG_CANCEL_CURRENT)
                }

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
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_MAX)
    }

    private fun layout(): Int {
        return when (actions.size) {
            in 1..2 -> R.layout.remote_notification_2_icons_right
            else -> R.layout.remote_notification_icons_at_bottom
        }
    }

    private fun applyToContent(contentView: RemoteViews, expandSubtitle:CharSequence? = null) {
        contentView.apply {
            setTextViewText(R.id.interactive_notification_title, contentTitle)
            expandSubtitle?.run {
                setTextViewText(R.id.interactive_notification_info, this)
                setViewVisibility(R.id.interactive_notification_info, View.VISIBLE)
            }?:setBoolean(R.id.interactive_notification_title, "setSingleLine", false)
        }
    }

    private fun applyChildren(contentView: RemoteViews, notificationId: Int,
                              bigView: Boolean, increment: Int): Int {
        var next = increment + 10
        actions.take(4).forEachIndexed { index, action ->
            val pendingAction = Intent(context, receiver)
                    .putExtra(ACTION_ID, action.id)
                    .putExtra(NOTIFICATION_ID, notificationId).let {
                        PendingIntent.getBroadcast(
                                context, next, it, PendingIntent.FLAG_CANCEL_CURRENT)
                    }
            val key = getActionKey(index)
            contentView.setTextViewText(key, action.text)
            contentView.setViewVisibility(key, View.VISIBLE)
            action.textSize?.also { contentView.setTextViewTextSize(key,TypedValue.COMPLEX_UNIT_SP, it) }
            if (bigView) contentView.setFloat(key, "setTextScaleX", 1.1f)
            contentView.setOnClickPendingIntent(key, pendingAction)
            next++
        }
        return next
    }

    private fun getActionKey(index: Int): Int {
        return when (index) {
            0 -> ACTION_1
            1 -> ACTION_2
            2 -> ACTION_3
            else -> ACTION_4
        }
    }

    companion object {

        private val ACTION_1 = R.id.interactive_notification_action_1
        private val ACTION_2 = R.id.interactive_notification_action_2
        private val ACTION_3 = R.id.interactive_notification_action_3
        private val ACTION_4 = R.id.interactive_notification_action_4

        private val CHANNEL_ID = "channelId"
        private val CHANNEL_NAME = "Interactive Notification"
    }
}
