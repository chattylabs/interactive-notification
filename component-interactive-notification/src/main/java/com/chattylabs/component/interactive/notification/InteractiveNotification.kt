package com.chattylabs.component.interactive.notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent

interface InteractiveNotification {

    interface Node {
        val id: String
    }

    //@Parcelize
    class Message(override val id: String,
                  val type: String = "",
                  val text: String) : Node//, Parcelable
    {
        var textSize: Float? = null
    }

    @Suppress("MemberVisibilityCanBePrivate")
    class Action(override val id: String, val text: String, val order: Int) :
            Node, Comparable<Action> {
        var textSize: Float? = null
        override fun compareTo(other: Action): Int = Integer.compare(order, other.order)
    }

    class ActionList : ArrayList<Action>(), Node {
        override val id: String = ""
    }

    class Utils {
        companion object {
            @JvmStatic
            fun getMessageId(intent: Intent): String? = intent.extras?.getString(MESSAGE_ID)

            @JvmStatic
            fun getMessageType(intent: Intent): String? = intent.extras?.getString(MESSAGE_TYPE)

            @JvmStatic
            fun getActionId(intent: Intent): String? = intent.extras?.getString(ACTION_ID)

            @JvmStatic
            fun getNotificationId(intent: Intent): Int = intent.extras?.getInt(NOTIFICATION_ID) ?: -1

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
internal const val MESSAGE_ID = "message_id"
internal const val MESSAGE_TYPE = "message_type"
internal const val ACTION_ID = "action_id"
internal const val NOTIFICATION_ID = "notification_id"
internal const val NOTIFICATION_DISMISSED = "notification_dismissed"