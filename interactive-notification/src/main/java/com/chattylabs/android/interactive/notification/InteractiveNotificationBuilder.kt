package com.chattylabs.android.interactive.notification

import android.content.BroadcastReceiver
import android.content.Context
import com.chattylabs.android.interactive.notification.InteractiveNotification.Action
import java.io.Serializable

internal class InteractiveNotificationBuilder(
        private val context: Context,
        private val graph: Serializable,
        private val node: InteractiveNotification.Node,
        private val receiver: Class<out BroadcastReceiver>,
        private val actions: List<Action>) {

    var expandSubtitle: CharSequence? = null

    fun build(): InteractiveNotification = InteractiveNotificationImpl(
            context, graph, node, receiver, expandSubtitle, actions)
}
