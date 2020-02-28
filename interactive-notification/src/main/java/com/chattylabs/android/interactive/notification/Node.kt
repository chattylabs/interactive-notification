package com.chattylabs.android.interactive.notification

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface Node {
    val id: String

    @Parcelize
    data class Message(override val id: String, val title: String, val description: String? = null,
                       var extras: HashMap<String, ArrayList<String>> = hashMapOf()) : Node, Parcelable

    @Parcelize
    data class Action(override val id: String, val text: String, val order: Int = 0,
                      var textSize: Float? = null) :
            Node, Comparable<Action>, Parcelable {

        override fun compareTo(other: Action): Int = order.compareTo(other.order)

        override fun equals(other: Any?): Boolean {
            return (this === other) || ((other is Action) && this.id == other.id)
        }

        override fun hashCode(): Int {
            return super.hashCode()
        }
    }

    class ActionList : ArrayList<Action>(), Node {
        override val id: String = "ActionList"
    }

    class Utils {
        companion object {

            @JvmStatic
            @Suppress("UNCHECKED_CAST")
            internal fun getGraph(intent: Intent?) = intent?.extras?.getSerializable(GRAPH) as HashMap<Node, ArrayList<Node>>

            @JvmStatic @Suppress("UNCHECKED_CAST")
            internal fun getIntentService(intent: Intent?) = intent?.extras?.getString(INTENT_SERVICE)?.
                    let { Class.forName(it) as Class<out IntentService> }

            @JvmStatic
            internal fun getChannelId(intent: Intent?) = intent?.extras?.getString(CHANNEL_ID)

            @JvmStatic
            internal fun getIcon(intent: Intent?) = intent?.extras?.getInt(ICON)

            @JvmStatic
            fun getMessageExtras(intent: Intent?) = intent?.extras?.getBundle(MESSAGE_EXTRA)

            @JvmStatic
            fun getMessageId(intent: Intent?) = intent?.extras?.getString(MESSAGE_ID)

            @JvmStatic
            fun getActionId(intent: Intent?) = intent?.extras?.getString(ACTION_ID)

            @JvmStatic
            fun getNotificationId(intent: Intent?) = intent?.extras?.getInt(NOTIFICATION_ID) ?: - 1
        }
    }

    companion object {

        internal fun processFlow(context: Context, intent: Intent?) {
            intent?.run {
                val actionId = Utils.getActionId(this)
                val component = Graph.Instance.get() as Graph
                if (actionId != null) {
                    if (component.graph.isNotEmpty()) {
                        component.currentNode = component.getNode(actionId)
                    } else {
                        component.graph = Utils.getGraph(this)
                        component.context = context.applicationContext
                        component.intentService = Utils.getIntentService(this)!!
                        component.channelId = Utils.getChannelId(this)!!
                        component.icon = Utils.getIcon(this)!!
                        component.notificationId = Utils.getNotificationId(this)
                        component.currentNode = component.getNode(actionId)
                    }
                    component.next()
                } else component.cancel()
            }
        }

        internal fun dismiss(context: Context, notificationId: Int) {
            val notificationManager: NotificationManager = context.applicationContext
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }
}

// internal use only
internal const val GRAPH = "GRAPH"
internal const val INTENT_SERVICE = "INTENT_SERVICE"
internal const val CHANNEL_ID = "CHANNEL_ID"
internal const val ICON = "ICON"
internal const val MESSAGE_EXTRA = "MESSAGE_EXTRA"
internal const val MESSAGE_ID = "MESSAGE_ID"
internal const val ACTION_ID = "ACTION_ID"
internal const val NOTIFICATION_ID = "NOTIFICATION_ID"