package com.chattylabs.component.interactive.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

interface InteractiveNotification {

    interface Node {
        val id: String
    }

    @Parcelize
    class Message(override val id: String,
                  val text: String,
                  val extras: HashMap<String, String> = hashMapOf()) : Node, Parcelable

    @Suppress("MemberVisibilityCanBePrivate")
    @Parcelize
    class Action(override val id: String, val text: String, val order: Int,
                 var textSize: Float? = null) :
            Node, Parcelable, Comparable<Action> {
        override fun compareTo(other: Action): Int = Integer.compare(order, other.order)
    }

    class ActionList : ArrayList<Action>(), Node {
        override val id: String = ""
    }

    class Utils {
        companion object {

            @JvmStatic
            fun getGraph(intent: Intent): Serializable? = intent.extras?.getSerializable(GRAPH)

            @JvmStatic
            fun getMessageExtras(intent: Intent): Bundle? = intent.extras?.getBundle(MESSAGE_EXTRA)

            @JvmStatic
            fun getMessageId(intent: Intent): String? = intent.extras?.getString(MESSAGE_ID)

            @JvmStatic
            fun getActionId(intent: Intent): String? = intent.extras?.getString(ACTION_ID)

            @JvmStatic
            fun getNotificationId(intent: Intent): Int = intent.extras?.getInt(NOTIFICATION_ID) ?: -1

            @JvmStatic
            fun isDismissed(intent: Intent): Boolean = intent.extras?.getBoolean(NOTIFICATION_DISMISSED)
                    ?: false

            @JvmStatic
            internal fun getReceiverClass(intent: Intent): Class<out BroadcastReceiver> {
                return intent.extras?.run {
                    @Suppress("UNCHECKED_CAST")
                    Class.forName(this.getString(RECEIVER_CLASS)!!) as Class<out BroadcastReceiver>
                }!!
            }
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
internal const val GRAPH = "graph"
internal const val RECEIVER_CLASS = "receiver_class"
internal const val MESSAGE_EXTRA = "message_extra"
internal const val MESSAGE_ID = "message_id"
internal const val ACTION_ID = "action_id"
internal const val NOTIFICATION_ID = "notification_id"
internal const val NOTIFICATION_DISMISSED = "notification_dismissed"