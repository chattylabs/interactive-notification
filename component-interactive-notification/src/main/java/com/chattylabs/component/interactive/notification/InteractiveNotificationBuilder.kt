package com.chattylabs.component.interactive.notification

import android.content.BroadcastReceiver
import android.content.Context
import com.chattylabs.component.interactive.notification.InteractiveNotification.Action

internal class InteractiveNotificationBuilder(
        private val context: Context,
        private val node: InteractiveNotification.Node,
        private val receiver: Class<out BroadcastReceiver>,
        private val actions: List<Action>) {

    var expandSubtitle: CharSequence? = null

    fun build(): InteractiveNotification = InteractiveNotificationImpl(
            context, node, receiver, expandSubtitle, actions)
}
