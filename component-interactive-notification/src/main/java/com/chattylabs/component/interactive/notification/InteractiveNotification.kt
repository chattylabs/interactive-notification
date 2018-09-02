package com.chattylabs.component.interactive.notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent

interface InteractiveNotification {

    interface Node {
        val id: String
    }

    class Message(override val id: String,
                  val text: String) : Node {
        var loaded: ((Node) -> Unit)? = null
        var textSize: Float? = null
    }

    class Action(override val id: String, val text: String, val order: Int) :
            Node, Comparable<Action> {
        var textSize: Float? = null
        override fun compareTo(other: Action): Int = other.order.compareTo(this.order)
    }

    class ActionList : ArrayList<Action>(), Node {
        override val id: String = ""
    }

    class Utils {
        companion object {
            @JvmStatic
            fun getActionId(intent: Intent): String? = intent.extras?.getString(ACTION_ID)

            @JvmStatic
            fun getNotificationId(intent: Intent): Int = intent.extras?.getInt(NOTIFICATION_ID) ?: 0

            @JvmStatic
            fun isDismissed(intent: Intent): Boolean = intent.extras?.getBoolean(NOTIFICATION_DISMISSED)
                    ?: false
        }
    }

    fun show(notificationId: Int)

    companion object {
        internal fun dismiss(context: Context, notificationId: Int) {
            val notificationManager: NotificationManager = context.applicationContext
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }
}

// internal use
internal const val ACTION_ID = "action_id"
internal const val NOTIFICATION_ID = "notification_id"
internal const val NOTIFICATION_DISMISSED = "notification_dismissed"