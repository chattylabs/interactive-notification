package com.chattylabs.android.interactive.notification

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.IdRes
import com.chattylabs.android.interactive.notification.Node.*
import java.io.Serializable

internal class NotificationMaker(
        private val context: Context,
        private val intentService: Class<out IntentService>,
        private val graph: Serializable,
        private val channelId: String,
        @IdRes private val icon: Int,
        private val message: Node,
        private val actions: List<Action>) {

    private val notificationManager: NotificationManager = context.applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun show(notificationId: Int) {
        notificationManager.notify(notificationId, create(notificationId).build())
    }

    private fun create(notificationId: Int): NotificationCompat.Builder {
        var index = 0

        val bundle = Bundle()
        (message as Message).extras.forEach { bundle.putStringArrayList(it.key, it.value) }

        val contentView = RemoteViews(context.packageName, layout()).also {
            applyContentTo(it, false)
            if (actions.size in 1..2)
                index = applyActionsTo(it, notificationId, false, index, bundle, graph)
        }

        val bigContentView = RemoteViews(context.packageName, layout()).also {
            applyContentTo(it, true)
            applyActionsTo(it, notificationId, true, index, bundle, graph)
        }

        val dismissIntent = Intent(context, NotificationService::class.java)
                .putExtra(GRAPH, graph)
                .putExtra(INTENT_SERVICE, intentService.canonicalName)
                .putExtra(CHANNEL_ID, channelId)
                .putExtra(ICON, icon)
                .putExtra(MESSAGE_ID, message.id)
                .putExtra(MESSAGE_EXTRA, bundle)
                .putExtra(NOTIFICATION_ID, notificationId)

        val dismissPendingIntent = PendingIntent.getService(
                context, notificationId, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setTicker(message.title)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(contentView)
                .setCustomBigContentView(bigContentView)
                .setCustomHeadsUpContentView(bigContentView)
                .setDeleteIntent(dismissPendingIntent)
                .setLocalOnly(true)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    private fun layout(): Int {
        return when (actions.size) {
            in 1..2 -> R.layout.remote_notification_2_icons_right
            else -> R.layout.remote_notification_icons_at_bottom
        }
    }

    private fun applyContentTo(contentView: RemoteViews, bigView: Boolean) {
        contentView.apply {
            var singleLine = false
            setTextViewText(R.id.interactive_notification_title, (message as Message).title)
            message.description?.run {
                singleLine = true
                setTextViewText(R.id.interactive_notification_info, this)
                setViewVisibility(R.id.interactive_notification_info, View.VISIBLE)
                setBoolean(R.id.interactive_notification_info, "setSingleLine", !bigView)
            }
            setBoolean(R.id.interactive_notification_title, "setSingleLine", singleLine)
        }
    }

    private fun applyActionsTo(contentView: RemoteViews, notificationId: Int,
                               bigView: Boolean, increment: Int, bundle: Bundle, graph: Serializable): Int {
        var next = increment + 10
        actions.take(4).forEachIndexed { index, action ->

            val actionIntent = Intent(context, NotificationService::class.java)
                    .putExtra(GRAPH,  graph)
                    .putExtra(INTENT_SERVICE, intentService.canonicalName)
                    .putExtra(CHANNEL_ID, channelId)
                    .putExtra(ICON, icon)
                    .putExtra(MESSAGE_ID, message.id)
                    .putExtra(MESSAGE_EXTRA, bundle)
                    .putExtra(ACTION_ID, action.id.removePrefix("${message.id}."))
                    .putExtra(NOTIFICATION_ID, notificationId)

            val actionPendingIntent = PendingIntent.getService(
                    context, next, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val viewId = getActionViewId(index)
            contentView.setTextViewText(viewId, action.text)
            contentView.setViewVisibility(viewId, View.VISIBLE)

            action.textSize?.also {
                contentView.setTextViewTextSize(viewId, TypedValue.COMPLEX_UNIT_SP, it) }

            if (bigView) contentView.setFloat(viewId, "setTextScaleX", 1.1f)

            contentView.setOnClickPendingIntent(viewId, actionPendingIntent)

            next++
        }
        return next
    }

    private fun getActionViewId(index: Int): Int {
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
    }
}
